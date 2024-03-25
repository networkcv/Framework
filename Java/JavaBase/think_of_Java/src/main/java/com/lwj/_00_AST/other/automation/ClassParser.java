package com.lwj._00_AST.other.automation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lwj._00_AST.other.automation.vo.ClassVO;
import com.lwj._00_AST.other.automation.vo.FieldVO;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaClass;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import com.thoughtworks.qdox.model.impl.DefaultJavaWildcardType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.security.CodeSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Date: 2024/3/17
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Slf4j
public class ClassParser {

    private static final Map<String, DefaultJavaClass> javaClassMap = Maps.newHashMap();

    public static ClassVO parse(Class<?> targetClass) {
        return parse(targetClass, null);
    }

    public static ClassVO parse(Class<?> targetClass, String packageScope) {
        ParsePrepare parsePrepare = ParsePrepare.builder()
                .targetClass(targetClass)
                .scope(ParsePrepare.Scope.builder()
                        .packageScope(packageScope)
                        .build())
                .build();
        return ClassParser.parse(parsePrepare);
    }

    public static ClassVO parse(ParsePrepare parsePrepare) {
        if (Objects.isNull(parsePrepare) || Objects.isNull(parsePrepare.getTargetClass())) {
            return null;
        }
        Class<?> targetClass = parsePrepare.getTargetClass();
        List<FieldVO> fields = Lists.newArrayList();
        ClassVO curClassVO = ClassVO.builder()
                .className(targetClass.getSimpleName())
                .genericQualifiedName(targetClass.getName())
                .clazz(targetClass)
                .fields(fields)
                .build();
        DefaultJavaClass javaClass = getFromJavaClassMap(targetClass);
        if (Objects.nonNull(javaClass)) {
            javaClass.getFields().forEach(field -> {
                ParsePrepare fieldPrepare = ParsePrepare.builder()
                        .targetClass(parseType(field))
                        .scope(parsePrepare.getScope())
                        .build();
                FieldVO fieldVO = FieldVO.builder()
                        .fieldName(field.getName())
                        .genericQualifiedName(field.getType().getGenericFullyQualifiedName())
                        .fieldType(parse(fieldPrepare))
                        .genericsTypes(parseGenericsTypes(field, parsePrepare))
                        .curClassType(curClassVO)
                        .describe(field.getComment())
                        .build();
                fields.add(fieldVO);
            });
        }
        return curClassVO;
    }

    private static List<ClassVO> parseGenericsTypes(JavaField field, ParsePrepare parsePrepare) {
        if (Objects.isNull(field.getType())) {
            return Lists.newArrayList();
        }
        JavaClass javaClassType = field.getType();
        if (!DefaultJavaParameterizedType.class.isAssignableFrom(javaClassType.getClass())) {
            return Lists.newArrayList();
        }

        DefaultJavaParameterizedType defaultJavaParameterizedType = (DefaultJavaParameterizedType) javaClassType;
        List<JavaType> actualTypeArguments = defaultJavaParameterizedType.getActualTypeArguments();
        if (CollectionUtils.isEmpty(actualTypeArguments)) {
            return Lists.newArrayList();
        }
        return actualTypeArguments.stream()
                .map(actualTypeArgument -> {
                    if (DefaultJavaWildcardType.class.isAssignableFrom(actualTypeArgument.getClass())) {
                        DefaultJavaWildcardType defaultJavaWildcardType = (DefaultJavaWildcardType) actualTypeArgument;
                        String genericFullyQualifiedName = defaultJavaWildcardType.getGenericFullyQualifiedName();
                        String boundType = DefaultJavaWildcardType.BoundType.EXTENDS.name().toLowerCase();
                        if (genericFullyQualifiedName.contains(boundType)) {
                            String[] split = genericFullyQualifiedName.split(boundType);
                            return split[1].trim();
                        }
                        boundType = DefaultJavaWildcardType.BoundType.SUPER.name().toLowerCase();
                        if (genericFullyQualifiedName.contains(boundType)) {
                            String[] split = genericFullyQualifiedName.split(boundType);
                            return split[1].trim();
                        }
                    }
                    return actualTypeArgument.getGenericFullyQualifiedName();
                })
                .map(ClassParser::parseType)
                .map(genericsType -> ParsePrepare.builder().targetClass(genericsType).scope(parsePrepare.getScope()).build())
                .map(ClassParser::parse)
                .collect(Collectors.toList());
    }

    private static DefaultJavaClass getFromJavaClassMap(Class<?> targetClass) {
        String className = "class " + targetClass.getName().replace("$", ".");
        DefaultJavaClass targetClassParsed;
        if (Objects.nonNull(targetClassParsed = javaClassMap.get(className))) {
            return targetClassParsed;
        }
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        try {
            String classSourceCodePath = getClassSourceCodePath(targetClass);
            if (Objects.isNull(classSourceCodePath)) {
                return null;
            }
            javaProjectBuilder.addSource(new File(classSourceCodePath));
        } catch (Exception e) {
            log.warn("getFromJavaClassMap warn targetClass:{}", targetClass.getName(), e);
            return null;
        }
        javaProjectBuilder.getClasses().forEach(javaClass -> javaClassMap.put(javaClass.toString(), (DefaultJavaClass) javaClass));
        return javaClassMap.get(className);
    }

    private static final Map<String, String> basic2WrapperType = Maps.newHashMap();

    static {
        basic2WrapperType.put("boolean", "java.lang.Boolean");
        basic2WrapperType.put("byte", "java.lang.Byte");
        basic2WrapperType.put("short", "java.lang.Short");
        basic2WrapperType.put("int", "java.lang.Integer");
        basic2WrapperType.put("long", "java.lang.Long");
        basic2WrapperType.put("float", "java.lang.Float");
        basic2WrapperType.put("double", "java.lang.Double");
        basic2WrapperType.put("char", "java.lang.Character");
    }

    private static Class<?> parseType(JavaField field) {
        JavaClass type = field.getType();
        String name = type.getName();
        String fullyQualifiedName = type.getFullyQualifiedName();
        String genericFullyQualifiedName = type.getGenericFullyQualifiedName();
        String simpleName = type.getSimpleName();
        String packageName = type.getPackageName();
        String binaryName = type.getBinaryName();
        if (field.getType().getName().equals(field.getType().getFullyQualifiedName())) {
            //基础类型
            return parseType(basic2WrapperType.get(name));
        }
        DefaultJavaClass defaultJavaClass = javaClassMap.get("class " + field.getType().getFullyQualifiedName());
        if (Objects.isNull(defaultJavaClass)) {
            return parseType(field.getType().getFullyQualifiedName());
        }
        if (Objects.nonNull(defaultJavaClass.getDeclaringClass())) {
            return parseType(field.getType().getGenericFullyQualifiedName());
        }
        return null;
    }

    private static Class<?> parseType(String genericFullyQualifiedName) {
        try {
            return Class.forName(genericFullyQualifiedName);
        } catch (ClassNotFoundException e) {
            log.warn("parseType warn genericFullyQualifiedName:{}", genericFullyQualifiedName, e);
            return null;
        }
    }

    public static String getClassSourceCodePath(Class<?> targetClass) {
        // 获取类的资源（即.class文件）的URL
        String classFilePath = targetClass.getProtectionDomain().getCodeSource().getLocation().getPath();

        // 如果是在JAR文件中
        if (classFilePath.endsWith(".jar")) {
            File jarFile = new File(classFilePath);
            System.out.println("Class is in JAR: " + jarFile.getAbsolutePath());
        } else {
            // 如果是在目录中
            System.out.println("Class is in directory: " + classFilePath);
        }

        CodeSource codeSource = targetClass.getProtectionDomain().getCodeSource();
        if (Objects.isNull(codeSource)) {
            return null;
        }
        System.out.println(targetClass.getName() + " a " + targetClass.getClassLoader().getResource("").getPath());
        String appAbsPath = codeSource.getLocation().getPath().split("target")[0];
        String sourceCodePath = "src/main/java/";
        String classPath = targetClass.getName().replace(".", "/").concat(".java");
        String path = appAbsPath + sourceCodePath + classPath;
        System.out.println(targetClass.getName() + " b " + path);
        return path;
    }
}
