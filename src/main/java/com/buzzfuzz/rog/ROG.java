package com.buzzfuzz.rog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

import com.buzzfuzz.rog.decisions.Config;
import com.buzzfuzz.rog.decisions.RNG;
import com.buzzfuzz.rog.traversal.ClassPkg;
import com.buzzfuzz.rog.traversal.InstanceDispatcher;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * Hello world!
 *
 */
public class ROG {

    private Reflections reflections;

    private boolean shouldLog;

    private Config defaultConfig;

    public ROG(String[] classPaths) {
        this(pathsToUrls(classPaths));
    }

    public ROG(URL[] urls) {
        this(new URLClassLoader(urls, ROG.class.getClass().getClassLoader()));
    }

    public ROG(URLClassLoader child) {
        this(new Reflections(
            new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forClassLoader(child))
            .addClassLoader(child).addScanners(
                new MethodAnnotationsScanner(),
                new SubTypesScanner(),
                new CarefulMethodParameterScanner())));
    }

    public ROG(Reflections reflections) {
        this.reflections = reflections;
        this.shouldLog = true;
    }

    public void setDefaultConfig(Config config) {
        this.defaultConfig = config;
    }

    private static URL[] pathsToUrls(String[] classPaths) {
        URL[] urls = new URL[classPaths.length];

        for (int i = 0; i < classPaths.length; i++) {
            try {
                urls[i] = Paths.get(classPaths[i]).toFile().toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public Reflections getReflections() {
        return this.reflections;
    }

    public void setShouldLog(boolean shouldLog) {
        this.shouldLog = shouldLog;
    }

    public Object[] getArgInstancesFor(Constructor<?> cnstr) {
        return getArgInstancesFor(cnstr, this.defaultConfig);
    }

    public Object[] getArgInstancesFor(Constructor<?> cnstr, Config config) {
        RNG rng = new RNG();
        rng.setConfig(config);
        return new InstanceDispatcher(rng, this).randomArgs(cnstr.getGenericParameterTypes());
    }

    public Object getArgInstancesFor(Method mth) {
        return getArgInstancesFor(mth, this.defaultConfig);
    }

    public Object getArgInstancesFor(Method mth, Config config) {
        RNG rng = new RNG();
        rng.setConfig(config);
        return new InstanceDispatcher(rng, this).randomArgs(mth.getGenericParameterTypes());
    }

    public Object getInstance(Class<?> target) {
        return getInstance(target, this.defaultConfig);
    }

    public Object getInstance(Class<?> target, Config config) {
        return getInstance(new ClassPkg(target, null), config);
    }

    public Object getInstance(ClassPkg pkg) {
        return getInstance(pkg, this.defaultConfig);
    }

    public Object getInstance(ClassPkg pkg, Config config) {
        RNG rng = new RNG();
        rng.setConfig(config);
        return new InstanceDispatcher(rng, this).getInstance(pkg);
    }


    public Object tryGetInstance(Class<?> target) {
        return tryGetInstance(target, this.defaultConfig);
    }

    public Object tryGetInstance(Class<?> target, Config config) {
        return tryGetInstance(new ClassPkg(target, null), config);
    }

    public Object tryGetInstance(ClassPkg pkg) {
        return tryGetInstance(pkg, this.defaultConfig);
    }

    public Object tryGetInstance(ClassPkg pkg, Config config) {
        RNG rng = new RNG();
        rng.setConfig(config);
        return new InstanceDispatcher(rng, this).tryGetInstance(pkg);
    }

    public void logCrash(Exception e, Config config) {
		Throwable t = e;
		while (t.getCause() != null && t.getCause().getStackTrace().length > 0) {
			t = t.getCause();
        }
        if (shouldLog) {
            System.out.println(t.getClass().getSimpleName());
        }
    }
}