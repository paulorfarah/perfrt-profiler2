package perfrt.profiler;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

class PerfrtProfilerAgent {

	public static void premain(String arguments, Instrumentation instrumentation) {
	    String[] agentArgs = arguments.trim().split("\\s*,\\s*");
	    final String packName = agentArgs[0];

	    new AgentBuilder.Default()
	    .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
	    .type((ElementMatchers.nameStartsWith(packName)))
	    .transform((builder, typeDescription, classLoader, module) -> builder
	            .method(ElementMatchers.any())
	            .intercept(Advice.withCustomMapping().bind(AgentArguments.class, arguments).to(TimerAdvice.class))
	    ).installOn(instrumentation);
	}}