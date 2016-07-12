package co.paralleluniverse.fibers.instrument;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

import org.osgi.framework.hooks.weaving.WeavingHook; 
import org.osgi.framework.hooks.weaving.WovenClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class EntityWeavingHookImpl implements WeavingHook, Instrumentation {
	private static final Logger LOG = LoggerFactory.getLogger(EntityWeavingHookImpl.class);
	
	protected List<ClassFileTransformer> transformers = new ArrayList<>();
	
	public EntityWeavingHookImpl() {
		JavaAgent.premain("", this);//vdcb
	}

	@Override
	public void weave(WovenClass wc) {
		String cn;
		String bsn;
		ClassLoader cl;
		byte[] result = null;
		boolean isTransformed = false;
		
		try {
			cn = wc.getClassName();
			bsn = wc.getBundleWiring().getBundle().getSymbolicName();
			if(cn.startsWith("org.eclipse.equinox") || cn.startsWith("org.osgi") || cn.startsWith("org.junit") || cn.startsWith("org.eclipse.jdt") || cn.startsWith("org.apache.felix")) {
				//System.out.println("-not weaving class " + cn);
				return;
			}
			
			cl = wc.getBundleWiring().getClassLoader();
			result = wc.getBytes();
			
			for (ClassFileTransformer transformer : transformers) {
	            try {
	                byte[] transformed = transformer.transform(cl, cn, null, null, result);
	                if (transformed != null) {
	                	if(Arrays.equals(result, transformed)) {
	                		//System.out.println("-fake weaving class " + cn);
	                	} else {
		                    result = transformed;
		                    isTransformed = true;
		                    //System.out.println("weaving class " + cn);
	                	}
	                } else {
	                	//System.out.println("-not weaving class  " + cn);
	                }
	            } catch (IllegalClassFormatException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
        if (isTransformed == true && result != null) {
            wc.setBytes(result);
        }
	}

	@Override
	public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
		transformers.add(transformer);
	}

	@Override
	public void addTransformer(ClassFileTransformer transformer) {
		transformers.add(transformer);
	}

	@Override
	public boolean removeTransformer(ClassFileTransformer transformer) {
		return transformers.remove(transformer);
	}

	@Override
	public boolean isRetransformClassesSupported() {
		return true;
	}

	@Override
	public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
		System.out.println("retransformClasses");
	}

	@Override
	public boolean isRedefineClassesSupported() {
		return true;
	}

	@Override
	public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
		System.out.println("redefineClasses");
	}

	@Override
	public boolean isModifiableClass(Class<?> theClass) {
		return true;
	}

	@Override
	public Class[] getAllLoadedClasses() {
		return null;
	}

	@Override
	public Class[] getInitiatedClasses(ClassLoader loader) {
		return null;
	}

	@Override
	public long getObjectSize(Object objectToSize) {
		return 0;
	}

	@Override
	public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
		System.out.println("appendToBootstrapClassLoaderSearch");
	}

	@Override
	public void appendToSystemClassLoaderSearch(JarFile jarfile) {
		System.out.println("appendToSystemClassLoaderSearch");
	}

	@Override
	public boolean isNativeMethodPrefixSupported() {
		return false;
	}

	@Override
	public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
		System.out.println("setNativeMethodPrefix");
	}
}
