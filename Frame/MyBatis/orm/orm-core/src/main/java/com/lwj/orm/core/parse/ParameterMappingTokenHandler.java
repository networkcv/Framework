package com.lwj.orm.core.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2021/11/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ParameterMappingTokenHandler implements TokenHandler {


    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    @Override
    public String handleToken(String content) {
        parameterMappings.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
        ParameterMapping parameterMapping = new ParameterMapping(content);
        return parameterMapping;
    }


    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
