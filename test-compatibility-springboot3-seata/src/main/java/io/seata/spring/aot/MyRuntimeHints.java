package io.seata.spring.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyRuntimeHints implements RuntimeHintsRegistrar {

//	private static final Logger LOGGER = LoggerFactory.getLogger(MyRuntimeHints.class);

	public static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		this.registerHintsForEureka1(hints);
		this.registerHintsForEureka2(hints);
	}

	private void registerHintsForEureka2(RuntimeHints hints) {

	}

	private void registerHintsForEureka1(RuntimeHints hints) {
		// Register following classes for `new DiscoveryClient(...)` .
		// See io.seata.discovery.registry.eureka.EurekaRegistryServiceImpl#getEurekaClient
		AotUtils.registerTypes(hints.reflection(),
				AotUtils.MEMBER_CATEGORIES_FOR_INSTANTIATE,
				"com.netflix.discovery.converters.XmlXStream",
				"com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider",
				"com.thoughtworks.xstream.mapper.EnumMapper",
				"com.thoughtworks.xstream.mapper.LambdaMapper",
				"com.thoughtworks.xstream.mapper.AnnotationMapper",
				"com.sun.ws.rs.ext.RuntimeDelegateImpl",
				"com.netflix.discovery.converters.jackson.DataCenterTypeInfoResolver"
		);
		AotUtils.registerTypes(hints.reflection(),
				AotUtils.MEMBER_CATEGORIES_FOR_INSTANTIATE_AND_INVOKE,
				"com.sun.jersey.api.client.RequestWriter"
		);
		AotUtils.registerTypesForSerialize(hints.reflection(),
				"com.netflix.appinfo.MyDataCenterInfo"
		);
		if (isPresent("com.thoughtworks.xstream.XStream")) {
			// See com.thoughtworks.xstream.XStream#setupAliases()
			AotUtils.registerTypes(hints.reflection(),
					new MemberCategory[0],
					"java.time.Clock$FixedClock",
					"java.time.Clock$OffsetClock",
					"java.time.Clock$SystemClock",
					"java.time.Clock$TickClock",
					"java.time.chrono.HijrahDate",
					"java.time.chrono.HijrahEra",
					"java.time.chrono.JapaneseDate",
					"java.time.chrono.JapaneseEra",
					"java.time.chrono.MinguoDate",
					"java.time.chrono.MinguoEra",
					"java.time.chrono.ThaiBuddhistDate",
					"java.time.chrono.ThaiBuddhistEra",
					"java.time.temporal.ValueRange",
					"java.time.temporal.WeekFields"
			);
			// See com.thoughtworks.xstream.XStream#setupConverters()
			AotUtils.registerTypes(hints.reflection(),
					AotUtils.MEMBER_CATEGORIES_FOR_INSTANTIATE,
					"com.thoughtworks.xstream.converters.extended.SubjectConverter",
					"com.thoughtworks.xstream.converters.extended.ThrowableConverter",
					"com.thoughtworks.xstream.converters.extended.StackTraceElementConverter",
					"com.thoughtworks.xstream.converters.extended.CurrencyConverter",
					"com.thoughtworks.xstream.converters.extended.RegexPatternConverter",
					"com.thoughtworks.xstream.converters.extended.CharsetConverter",
					"com.thoughtworks.xstream.converters.extended.DurationConverter",
					"com.thoughtworks.xstream.converters.enums.EnumConverter",
					"com.thoughtworks.xstream.converters.enums.EnumSetConverter",
					"com.thoughtworks.xstream.converters.enums.EnumMapConverter",
					"com.thoughtworks.xstream.converters.basic.StringBuilderConverter",
					"com.thoughtworks.xstream.converters.basic.UUIDConverter",
					"com.thoughtworks.xstream.converters.extended.PathConverter",
					"com.thoughtworks.xstream.converters.time.ChronologyConverter",
					"com.thoughtworks.xstream.converters.time.DurationConverter",
					"com.thoughtworks.xstream.converters.time.HijrahDateConverter",
					"com.thoughtworks.xstream.converters.time.JapaneseDateConverter",
					"com.thoughtworks.xstream.converters.time.JapaneseEraConverter",
					"com.thoughtworks.xstream.converters.time.InstantConverter",
					"com.thoughtworks.xstream.converters.time.LocalDateConverter",
					"com.thoughtworks.xstream.converters.time.LocalDateTimeConverter",
					"com.thoughtworks.xstream.converters.time.LocalTimeConverter",
					"com.thoughtworks.xstream.converters.time.MinguoDateConverter",
					"com.thoughtworks.xstream.converters.time.MonthDayConverter",
					"com.thoughtworks.xstream.converters.time.OffsetDateTimeConverter",
					"com.thoughtworks.xstream.converters.time.OffsetTimeConverter",
					"com.thoughtworks.xstream.converters.time.PeriodConverter",
					"com.thoughtworks.xstream.converters.time.SystemClockConverter",
					"com.thoughtworks.xstream.converters.time.ThaiBuddhistDateConverter",
					"com.thoughtworks.xstream.converters.time.ValueRangeConverter",
					"com.thoughtworks.xstream.converters.time.WeekFieldsConverter",
					"com.thoughtworks.xstream.converters.time.YearConverter",
					"com.thoughtworks.xstream.converters.time.YearMonthConverter",
					"com.thoughtworks.xstream.converters.time.ZonedDateTimeConverter",
					"com.thoughtworks.xstream.converters.time.ZoneIdConverter",
					"com.thoughtworks.xstream.converters.reflection.LambdaConverter"
			);
			// See com.thoughtworks.xstream.core.util.SerializationMembers#NO_METHOD
			AotUtils.registerTypes(hints.reflection(),
					new MemberCategory[]{MemberCategory.INVOKE_DECLARED_METHODS},
					"com.thoughtworks.xstream.core.util.SerializationMembers"
			);
		}
	}
}
