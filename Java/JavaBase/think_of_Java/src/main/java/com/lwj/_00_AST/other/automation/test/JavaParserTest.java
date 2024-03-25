package com.lwj._00_AST.other.automation.test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.collect.Lists;
import com.lwj._00_AST.other.automation.ClassParser;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 2024/3/15
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class JavaParserTest {
    public static void main(String[] args) throws Exception {

        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> parse = parser.parse(new File(ClassParser.getClassSourceCodePath(Student.class)));
        CompilationUnit compilationUnit = parse.getResult().get();
        List<Node> childNodes = compilationUnit.getChildNodes();

        childNodes = childNodes.stream().sorted(Comparator.comparingInt(node -> node.getRange().get().getLineCount())).collect(Collectors.toList());
        PackageDeclaration packageNode = null;
        List<ImportDeclaration> importNodeList = Lists.newArrayList();
        ClassOrInterfaceDeclaration classNode = null;
        for (Node node : childNodes) {
            if (node instanceof PackageDeclaration) {
                packageNode = (PackageDeclaration) node;
            }
            if (node instanceof ImportDeclaration) {
                //可能存在导入*号的情况
                importNodeList.add((ImportDeclaration) node);
            }
            if (node instanceof ClassOrInterfaceDeclaration) {
                classNode = (ClassOrInterfaceDeclaration) node;
            }
        }
        //获取名称
        classNode.getName();
        //获取修饰
        classNode.getModifiers();
        //获取注解
        classNode.getAnnotations();
        //获取成员如 FieldDeclaration、MethodDeclaration
        NodeList<BodyDeclaration<?>> members = classNode.getMembers();


        //获取注释
        classNode.getComment();
    }
}
