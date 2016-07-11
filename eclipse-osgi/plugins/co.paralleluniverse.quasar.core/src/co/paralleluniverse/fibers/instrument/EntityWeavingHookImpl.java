package co.paralleluniverse.fibers.instrument;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
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
		JavaAgent.premain(null, this);
	}

	@Override
	public void weave(WovenClass wc) {
		String cn;
		ClassLoader cl;
		byte[] result = null;
		
		try {
			cn = wc.getClassName(); 
			System.out.println("Weaving class " + cn);
			cl = wc.getBundleWiring().getClassLoader();
			result = wc.getBytes();
			
			for (ClassFileTransformer transformer : transformers) {
	            try {
	                byte[] transformed = transformer.transform(cl, cn, null, null, result);
	                if (transformed != null) {
	                    result = transformed;
	                }
	            } catch (IllegalClassFormatException e) {
	                e.printStackTrace();
	            }
	        }
		} catch (Exception e) {
            e.printStackTrace();
        }
		
        if (result != null) {
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
		return false;
	}

	@Override
	public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
	}

	@Override
	public boolean isRedefineClassesSupported() {
		return false;
	}

	@Override
	public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
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
	}

	@Override
	public void appendToSystemClassLoaderSearch(JarFile jarfile) {
	}

	@Override
	public boolean isNativeMethodPrefixSupported() {
		return false;
	}

	@Override
	public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
	}
}
