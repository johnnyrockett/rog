package com.buzzfuzz.rog;

import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.AbstractScanner;

import java.util.List;

/** scans methods/constructors and indexes parameters, return type and parameter annotations */
@SuppressWarnings("unchecked")
public class CarefulMethodParameterScanner extends AbstractScanner {

    @Override
    public void scan(Object cls) {
        @SuppressWarnings("rawtypes")
		final MetadataAdapter md = getMetadataAdapter();

        for (Object method : md.getMethods(cls)) {

            if (md.getMethodName(method).indexOf('$') != -1) {
                continue;
            }
        	
            String signature = md.getParameterNames(method).toString();
            if (acceptResult(signature)) {
                getStore().put(signature, md.getMethodFullKey(cls, method));
            }

            String returnTypeName = md.getReturnTypeName(method);
            if (acceptResult(returnTypeName)) {
                getStore().put(returnTypeName, md.getMethodFullKey(cls, method));
            }

            List<String> parameterNames = md.getParameterNames(method);
            for (int i = 0; i < parameterNames.size(); i++) {
                for (Object paramAnnotation : md.getParameterAnnotationNames(method, i)) {
                    if (acceptResult((String) paramAnnotation)) {
                        getStore().put((String) paramAnnotation, md.getMethodFullKey(cls, method));
                    }
                }
            }
        }
    }
}
