# Minimal Reproducible Example
### ScopedArtifact.CLASSES returning JaCoCo instrumented class files

This sample project can be used to reproduce a problem where the new Android Gradle Plugin 8.0 will return
JaCoCo instrumented classes when using the newly introduced `ScopedArtifact.CLASSES` provider.

This is potentially a bug as it does not allow users of `ScopedArtifact.CLASSES` to provide non-instrumented classes 
to for example the `JacocoReport` task (`org.gradle.testing.jacoco.tasks.JacocoReport.classDirectories`).

**The problem can be reproduced in at least 2 ways:**

1. Running the custom task `app:debugGetAllClasses` which should thrown an error saying
   > Provided directories contain jacoco instrumented classes!

   _Code for this can be found in [`app/build.gradle.kts`](app/build.gradle.kts)_
2. Running the unit test [`TestClassUnderTest.test_add()`](app/src/test/java/org/neotech/example/scopedartifact/TestClassUnderTest.kt#L9), which will complete successfully but
   Android Studio will show the following trace:

```
java.lang.instrument.IllegalClassFormatException: Error while instrumenting org/neotech/example/scopedartifact/ClassUnderTest with JaCoCo 0.8.8.202204050719/5dcf34a.
	at org.jacoco.agent.rt.internal_b6258fc.CoverageTransformer.transform(CoverageTransformer.java:94)
	at java.instrument/java.lang.instrument.ClassFileTransformer.transform(Unknown Source)
	at java.instrument/sun.instrument.TransformerManager.transform(Unknown Source)
	at java.instrument/sun.instrument.InstrumentationImpl.transform(Unknown Source)
	at java.base/java.lang.ClassLoader.defineClass1(Native Method)
	at java.base/java.lang.ClassLoader.defineClass(Unknown Source)
	at java.base/java.security.SecureClassLoader.defineClass(Unknown Source)
	at java.base/jdk.internal.loader.BuiltinClassLoader.defineClass(Unknown Source)
	at java.base/jdk.internal.loader.BuiltinClassLoader.findClassOnClassPathOrNull(Unknown Source)
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClassOrNull(Unknown Source)
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(Unknown Source)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(Unknown Source)
	at java.base/java.lang.ClassLoader.loadClass(Unknown Source)
	at org.neotech.example.scopedartifact.TestClassUnderTest.test_add(TestClassUnderTest.kt:10)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.base/java.lang.reflect.Method.invoke(Unknown Source)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:108)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:40)
	at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:60)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:52)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.base/java.lang.reflect.Method.invoke(Unknown Source)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at jdk.proxy2/jdk.proxy2.$Proxy5.processTestClass(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker$2.run(TestWorker.java:176)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.executeAndMaintainThreadName(TestWorker.java:129)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:100)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:60)
	at org.gradle.process.internal.worker.child.ActionExecutionWorker.execute(ActionExecutionWorker.java:56)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:113)
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:65)
	at worker.org.gradle.process.internal.worker.GradleWorkerMain.run(GradleWorkerMain.java:69)
	at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)
Caused by: java.io.IOException: Error while instrumenting org/neotech/example/scopedartifact/ClassUnderTest with JaCoCo 0.8.8.202204050719/5dcf34a.
	at org.jacoco.agent.rt.internal_b6258fc.core.instr.Instrumenter.instrumentError(Instrumenter.java:161)
	at org.jacoco.agent.rt.internal_b6258fc.core.instr.Instrumenter.instrument(Instrumenter.java:111)
	at org.jacoco.agent.rt.internal_b6258fc.CoverageTransformer.transform(CoverageTransformer.java:92)
	... 56 more
Caused by: java.lang.IllegalStateException: Cannot process instrumented class org/neotech/example/scopedartifact/ClassUnderTest. Please supply original non-instrumented classes.
	at org.jacoco.agent.rt.internal_b6258fc.core.internal.instr.InstrSupport.assertNotInstrumented(InstrSupport.java:238)
	at org.jacoco.agent.rt.internal_b6258fc.core.internal.instr.ClassInstrumenter.visitField(ClassInstrumenter.java:56)
	at org.jacoco.agent.rt.internal_b6258fc.asm.ClassVisitor.visitField(ClassVisitor.java:338)
	at org.jacoco.agent.rt.internal_b6258fc.asm.ClassReader.readField(ClassReader.java:1137)
	at org.jacoco.agent.rt.internal_b6258fc.asm.ClassReader.accept(ClassReader.java:739)
	at org.jacoco.agent.rt.internal_b6258fc.asm.ClassReader.accept(ClassReader.java:424)
	at org.jacoco.agent.rt.internal_b6258fc.core.instr.Instrumenter.instrument(Instrumenter.java:91)
	at org.jacoco.agent.rt.internal_b6258fc.core.instr.Instrumenter.instrument(Instrumenter.java:109)
	... 57 more
```