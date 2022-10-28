package com.github.automeican;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        String localPath = "E:\\ccc\\";
        FastAutoGenerator.create("jdbc:sqlite::resource:db/meican.db", "username", "password")
                .globalConfig(builder -> {
                    builder.author("liyongbing") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(localPath+"auto-meican/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.github.automeican") // 设置父包名
                            .moduleName("dao") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, localPath+"auto-meican/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("meican_account_dish_check") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .entityBuilder().enableLombok().logicDeleteColumnName("deleted")
                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
