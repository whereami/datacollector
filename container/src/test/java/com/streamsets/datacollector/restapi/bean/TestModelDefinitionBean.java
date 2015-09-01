/**
 * (c) 2014 StreamSets, Inc. All rights reserved. May not
 * be copied, modified, or distributed in whole or part without
 * written consent of StreamSets, Inc.
 */
package com.streamsets.datacollector.restapi.bean;

import com.streamsets.datacollector.config.ConfigDefinition;
import com.streamsets.datacollector.config.ModelType;
import com.streamsets.datacollector.el.ElConstantDefinition;
import com.streamsets.datacollector.el.ElFunctionDefinition;
import com.streamsets.datacollector.restapi.bean.BeanHelper;
import com.streamsets.datacollector.restapi.bean.ModelDefinitionJson;
import com.streamsets.pipeline.api.ConfigDef;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestModelDefinitionBean {

  @Test(expected = NullPointerException.class)
  public void testModelDefinitionBeanNull() {
    new ModelDefinitionJson(null);
  }

  @Test
  public void testModelDefinitionBean() {
    List<String> values = new ArrayList<>();
    values.add("v1");
    values.add("v2");

    List<String> labels = new ArrayList<>();
    labels.add("V ONE");
    labels.add("V TWO");

    List<Object> triggeredBy = new ArrayList<>();
    triggeredBy.add("X");
    triggeredBy.add("Y");
    triggeredBy.add("Z");

    List< ConfigDefinition > configDefinitions = new ArrayList<>();
    configDefinitions.add(new ConfigDefinition("int", ConfigDef.Type.NUMBER, "l2", "d2", "-1", true, "g", "intVar", null, "A",
      triggeredBy, 0, Collections.<ElFunctionDefinition>emptyList(), Collections.<ElConstantDefinition>emptyList(),
      Long.MIN_VALUE, Long.MAX_VALUE, "text/plain", 0, Collections.<Class> emptyList(), ConfigDef.Evaluation.IMPLICIT, null));

    com.streamsets.datacollector.config.ModelDefinition modelDefinition =
      new com.streamsets.datacollector.config.ModelDefinition(ModelType.LIST_BEAN,
                                                         "valuesProviderClass", values,labels, String.class,
                                                         configDefinitions);

    ModelDefinitionJson modelDefinitionJsonBean = new ModelDefinitionJson(modelDefinition);

    Assert.assertEquals(modelDefinition.getValues(), modelDefinitionJsonBean.getValues());
    Assert.assertEquals(modelDefinition.getLabels(), modelDefinitionJsonBean.getLabels());
    Assert.assertEquals(modelDefinition.getModelType(), BeanHelper.unwrapModelType(modelDefinitionJsonBean.getModelType()));
    Assert.assertEquals(modelDefinition.getValuesProviderClass(), modelDefinitionJsonBean.getValuesProviderClass());
  }

}
