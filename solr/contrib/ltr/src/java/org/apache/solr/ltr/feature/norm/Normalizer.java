package org.apache.solr.ltr.feature.norm;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.search.Explanation;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.ltr.feature.norm.impl.IdentityNormalizer;
import org.apache.solr.ltr.feature.norm.impl.StandardNormalizer;
import org.apache.solr.ltr.util.NamedParams;
import org.apache.solr.ltr.util.NormalizerException;
import org.apache.solr.util.SolrPluginUtils;

/**
 * A normalizer normalizes the value of a feature. Once that the feature values
 * will be computed, the normalizer will be applied and the resulting values
 * will be received by the model.
 *
 * @see IdentityNormalizer
 * @see StandardNormalizer
 *
 */
public abstract class Normalizer {

  NamedParams params;

  public NamedParams getParams() {
    return params;
  }

  public void init(NamedParams params) throws NormalizerException {
    this.params = params;
    SolrPluginUtils.invokeSetters(this, params.entrySet());
  }

  public abstract float normalize(float value);

  public Explanation explain(Explanation explain) {
    final float normalized = normalize(explain.getValue());
    String explainDesc = "normalized using " + getClass().getSimpleName();
    if (params != null) {
      explainDesc += " [params " + params + "]";
    }

    return Explanation.match(normalized, explainDesc, explain);
  }

  public static Normalizer getInstance(String type, NamedParams params,
    SolrResourceLoader solrResourceLoader) {
    final Normalizer f = solrResourceLoader.newInstance(type, Normalizer.class);
    if (params == null) {
      params = NamedParams.EMPTY;
    }
    f.init(params);
    return f;

  }

}