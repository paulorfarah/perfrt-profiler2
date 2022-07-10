package perfrt.profiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bytebuddy.asm.Advice;

	
public class TimerAdvice {   
	    
	@Advice.OnMethodEnter
	public static OnMethodEnterReturn enter(@Advice.Origin String method, @AgentArguments String agentArguments) {
		String[] agentArgs = agentArguments.trim().split("\\s*,\\s*");
		
		OnMethodEnterReturn onEnterValues = new OnMethodEnterReturn();
		onEnterValues.setPackageName(agentArgs[0]);
		onEnterValues.setCommitHash(agentArgs[1]);
		onEnterValues.setIdRun(Integer.parseInt(agentArgs[2]));
		onEnterValues.setStartTime(System.currentTimeMillis());
		String className = parseClassName(method);
		System.out.println("onEnter - Class: " + className + " method: " + method);
		onEnterValues.setIdMethod(1);
		return onEnterValues;
	}
	
	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void exit(@Advice.Origin String method
	        , @Advice.Enter OnMethodEnterReturn onEnterValues 	
	){
		String packName = onEnterValues.getPackageName(); 
		String className = parseClassName(method).replace(packName, "") + ".java";
		long start = onEnterValues.getStartTime();
		long end =  System.currentTimeMillis();
		long duration = end - start; 
		String returnedType = "teste";
		System.out.println("onExit - Class: " + className + " method: " + method + " start: " + start + " end: " + end + " duration: " + duration + "retValue:" );
	}
	
	public static String parseClassName(String method) {
		String className = null;
		Pattern p = Pattern.compile("[^\\s]*\\(");
		Matcher m = p.matcher(method);
		if (m.find()) {
		    String methodName = (String)m.group(0).toString();
		    
		    String[] methodAux = methodName.split("\\.");
		    if(methodAux.length > 0) {
				className = methodAux[0];
				for(int i = 1; i < methodAux.length-1; i++) {
					className += "." + methodAux[i];
				}
		    }else {
		    	className = methodName;
		    }
		}
		String last = className.substring(className.lastIndexOf('.') + 1);
		className = last + ".java";
		return className;
	}
}



