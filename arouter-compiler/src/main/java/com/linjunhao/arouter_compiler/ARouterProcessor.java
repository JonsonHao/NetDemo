package com.linjunhao.arouter_compiler;

import com.google.auto.service.AutoService;
import com.linjunhao.arouter_annotation.ARouter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/3/15
 * @desc:
 */
@AutoService(Processor.class)
public class ARouterProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Collections.singletonList(ARouter.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ARouter.class)) {
            MethodSpec main = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(String[].class, "args")
//                    .addStatement("$T.out.println($S)", System.class, "Hello JavaSpot!")
                    .addStatement("int total = 0")
                    .beginControlFlow("for (int i = 0; i < 10; i++)")
                    .addStatement("total += i")
                    .endControlFlow()
                    .build();
            MethodSpec test = MethodSpec.methodBuilder("test")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("sum0to20()")
                    .addStatement("sum10to15()")
                    .build();

            ClassName list = ClassName.get("java.util", "List");
            ClassName arrayList = ClassName.get("java.util", "ArrayList");
            TypeName listOfHoverboards = ParameterizedTypeName.get(list, ClassName.get(String.class));

            MethodSpec beyond = MethodSpec.methodBuilder("beyond")
                    .returns(listOfHoverboards)
                    .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
                    .addStatement("result.add(new $T())", String.class)
                    .addStatement("result.add(new $T())", String.class)
                    .addStatement("result.add(new $T())", String.class)
                    .addStatement("return result")
                    .build();

            // ??????????????????
            MethodSpec flux = MethodSpec.methodBuilder("flux")
                    .addModifiers(Modifier.ABSTRACT, Modifier.PROTECTED)
                    .build();

            // ??????????????????
            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(String.class, "greeting")
                    .addStatement("this.$N = $N", "greeting", "greeting")
                    .build();

            // try/catch/finally
            MethodSpec tryCatch = MethodSpec.methodBuilder("tryCatch")
                    .addModifiers(Modifier.PUBLIC)
                    .beginControlFlow("try")
                    .addStatement("throw new $T($S)", Exception.class, "Failed")
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("e.printStackTrace()")
                    .nextControlFlow("finally")
                    .addStatement("$T.out.println($S)", System.class, "do something")
                    .endControlFlow()
                    .build();

            // ??????
            FieldSpec android = FieldSpec.builder(String.class, "android")
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("$S", "android")
                    .build();

            String className = "HelloWorldDemo";
            TypeSpec helloWorld = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    // ????????????
                    .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
                    .addField(android)
                    .addMethod(main)
                    .addMethod(computeRange("sum0to20", 0, 20, "+"))
                    .addMethod(computeRange("sum10to15", 10, 15, "*"))
                    .addMethod(test)
                    .addMethod(beyond)
                    .addMethod(flux)
                    .addMethod(constructor)
                    .addMethod(tryCatch)
                    .build();

            // ?????????
            JavaFile javaFile = JavaFile.builder("com.example.linjunhao", helloWorld)
                    .build();
            try {
                messager.printMessage(Diagnostic.Kind.NOTE, "?????? -- " + className);
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 1")
                .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++)")
                .addStatement("result = result " + op + " i")
                .endControlFlow()
                .addStatement("return result")
                .build();
    }

}
