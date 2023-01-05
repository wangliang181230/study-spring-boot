/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.seata.spring.aot;

import io.seata.config.Configuration;
import io.seata.rm.datasource.sql.struct.TableRecords;
import io.seata.rm.datasource.undo.BranchUndoLog;
import io.seata.rm.datasource.undo.SQLUndoLog;
import io.seata.saga.statelang.parser.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.sql.DataSource;

import static io.seata.spring.aot.AotUtils.ALL_MEMBER_CATEGORIES;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS;

class SeataRuntimeHints implements RuntimeHintsRegistrar {

	private static final Logger LOGGER = LoggerFactory.getLogger(SeataRuntimeHints.class);

	private static final Set<String> SERVICES_FILENAMES = new HashSet<>();

	static {
		SERVICES_FILENAMES.add("com.alibaba.dubbo.rpc.Filter");
		SERVICES_FILENAMES.add("com.alipay.sofa.rpc.filter.Filter");
		SERVICES_FILENAMES.add("com.taobao.hsf.invocation.filter.RPCFilter");
		SERVICES_FILENAMES.add("com.weibo.api.motan.filter.Filter");
	}

	@Override
	public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
		hints.proxies().registerJdkProxy(
				DataSource.class,
				Configuration.class
		);

		// hint services
		Resource[] resources = ResourceUtil.getResources("classpath*:META-INF/services/*");
		System.out.println("services resources.length: " + resources.length);
		for (Resource resource : resources) {
			System.out.println(resource.getFilename());
			if (!isSeataServices(resource)) {
				continue;
			}

			try (InputStreamReader isr = new InputStreamReader(resource.getInputStream());
				 BufferedReader br = new BufferedReader(isr)) {
				br.lines().forEach(className -> {
					registerReflectionType(hints, className);
				});
			} catch (IOException e) {
				System.out.println("--- read error: " + resource.getFilename() + ", " + e.getMessage());
			}
		}

		registerReflectionType(hints,
				"io.seata.sqlparser.druid.DruidDbTypeParserImpl",
				"io.seata.sqlparser.druid.DruidSQLRecognizerFactoryImpl",
				"io.seata.serializer.protobuf.ProtobufSerializer",
				"io.seata.sqlparser.antlr.mysql.AntlrMySQLRecognizerFactory"
		);

		hints.reflection().registerType(BranchUndoLog.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", BranchUndoLog.class.getName());
		hints.reflection().registerType(SQLUndoLog.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", BranchUndoLog.class.getName());

		hints.reflection().registerType(TableRecords.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", TableRecords.class.getName());
		hints.reflection().registerType(TableRecords.EmptyTableRecords.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", TableRecords.EmptyTableRecords.class.getName());
		hints.reflection().registerType(io.seata.rm.datasource.sql.struct.Row.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", io.seata.rm.datasource.sql.struct.Row.class.getName());
		hints.reflection().registerType(io.seata.rm.datasource.sql.struct.Field.class, ALL_MEMBER_CATEGORIES);
		LOGGER.info("register reflection type: {}", io.seata.rm.datasource.sql.struct.Field.class.getName());

		// caffeine中的类：类名全部为大写的类
		Set<Class<?>> classes = getClassesByPackage("com.github.benmanes.caffeine.cache");
		if (classes.size() > 0) {
			for (Class<?> clazz : classes) {
				String simpleClassName = clazz.getSimpleName();
				if (simpleClassName.length() > 0 && simpleClassName.toUpperCase().equals(simpleClassName)) {
					registerReflectionType(hints, clazz);
				}
			}
		}

		hints.resources().registerPattern("lib/sqlparser/druid.jar");
		hints.resources().registerPattern("META-INF/services/io.seata.*");
		hints.resources().registerPattern("META-INF/seata/io.seata.*");
		for (String servicesFileName : SERVICES_FILENAMES) {
			hints.resources().registerPattern("META-INF/services/" + servicesFileName);
			hints.resources().registerPattern("META-INF/seata/" + servicesFileName);
		}
	}

	private boolean isSeataServices(Resource resource) {
		if (resource.getFilename() == null) {
			return false;
		}

		if (resource.getFilename().startsWith("io.seata")) {
			return true;
		}

		return SERVICES_FILENAMES.contains(resource.getFilename());
	}

	private void registerReflectionType(RuntimeHints hints, String... classNames) {
		ReflectionHints reflectionHints = hints.reflection();
		for (String className : classNames) {
			try {
				reflectionHints.registerType(Class.forName(className),
						INTROSPECT_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_CONSTRUCTORS);
				LOGGER.info("register reflection type: {}", className);
			} catch (ClassNotFoundException | NoClassDefFoundError e) {
				LOGGER.info("Class not found '{}', can't register type to 'reflect-config.json'.", className);
			}
		}
	}

	private void registerReflectionType(RuntimeHints hints, Class<?>... classes) {
		ReflectionHints reflectionHints = hints.reflection();
		for (Class<?> clazz : classes) {
			reflectionHints.registerType(clazz,
					INTROSPECT_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_CONSTRUCTORS);
			LOGGER.info("register reflection type: {}", clazz.getName());
		}
	}

	public static Set<Class<?>> getClassesByPackage(String pack) {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// System.err.println("file类型的扫描");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					// System.err.println("jar类型的扫描");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection)url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											// log
											// .error("添加用户自定义视图类错误
											// 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
														Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
		File[] dirfiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
		// 循环所有文件
		assert dirfiles != null;
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(
							Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
}
