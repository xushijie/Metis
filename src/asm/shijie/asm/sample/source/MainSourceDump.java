package asm.shijie.asm.sample.source;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.*;



public class MainSourceDump  extends ClassLoader implements Opcodes{

	public static byte[] dump() throws Exception {

		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;
		AnnotationVisitor av0;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER,
				"shijie/asm/sample/source/MainSource", null,
				"java/lang/Object", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "sayHello",
					"(Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
					"Ljava/io/PrintStream;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
					"(Ljava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
					"([Ljava/lang/String;)V", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, "shijie/asm/sample/source/MainSource");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL,
					"shijie/asm/sample/source/MainSource", "<init>", "()V",
					false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL,
					"shijie/asm/sample/source/MainSource", "sayHello",
					"(Ljava/lang/String;)V", false);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
	
	@Override
	protected synchronized Class<?> loadClass(final String name,
			final boolean resolve) throws ClassNotFoundException {
		if(name.startsWith("java")){
			return super.loadClass(name, resolve);
		}
		byte[] b = null;
		try {
			b = dump();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//InputStream stream = new ByteArrayInputStream(b);
		// adapts the class on the fly
//		try {
//			ClassReader cr = new ClassReader(is);
//			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//			ClassVisitor cv = new ClassAdapter(cw);
//			cr.accept(cv, 0);
//			b = cw.toByteArray();
//		} catch (Exception e) {
//			throw new ClassNotFoundException(name, e);
//		}
//
//		// optional: stores the adapted class on disk
//		try {
//			FileOutputStream fos = new FileOutputStream("/tmp/"+resource.substring(resource.lastIndexOf("/")) + ".class");
//			fos.write(b);
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return defineClass(name, b, 0, b.length);
	}
		
	
	public static void main(String[] args){
		try {
			Class<?> loader = new MainSourceDump().loadClass("shijie.asm.sample.source.MainSource");
		   
			Method m;
			try {
				m = loader.getMethod("main", new Class<?>[] { String[].class });
			    String[] applicationArgs = new String[args.length - 1];
			    System.arraycopy(args, 1, applicationArgs, 0, applicationArgs.length);
			    m.invoke(null, new Object[] { args });
			        
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	        
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}