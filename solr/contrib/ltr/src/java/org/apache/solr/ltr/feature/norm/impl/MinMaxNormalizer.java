package org.apache.solr.ltr.feature.norm.impl;

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

import org.apache.solr.ltr.feature.norm.Normalizer;
import org.apache.solr.ltr.util.NamedParams;
import org.apache.solr.ltr.util.NormalizerException;

public class MinMaxNormalizer extends Normalizer {

  private float min = Float.NEGATIVE_INFINITY;
  private float max = Float.POSITIVE_INFINITY;
  private float delta = max - min;

  private void updateDelta() {
    delta = max - min;
  }

  public float getMin() {
    return min;
  }

  public void setMin(float min) {
    this.min = min;
    updateDelta();
  }

  public void setMin(String min) {
    this.min = Float.parseFloat(min);
    updateDelta();
  }

  public float getMax() {
    return max;
  }

  public void setMax(float max) {
    this.max = max;
    updateDelta();
  }

  public void setMax(String max) {
    this.max = Float.parseFloat(max);
    updateDelta();
  }

  @Override
  public void init(NamedParams params) throws NormalizerException {
    super.init(params);
    if (!params.containsKey("min")) {
      throw new NormalizerException(
          "missing required param [min] for normalizer MinMaxNormalizer");
    }
    if (!params.containsKey("max")) {
      throw new NormalizerException(
          "missing required param [max] for normalizer MinMaxNormalizer");
    }
    try {
      min = params.getFloat("min");

      max = params.getFloat("max");

    } catch (final Exception e) {
      throw new NormalizerException(
          "invalid param value for normalizer MinMaxNormalizer", e);
    }

    updateDelta();
    if (delta <= 0) {
      throw new NormalizerException(
          "invalid param value for MinMaxNormalizer, min must be lower than max ");
    }
  }

  @Override
  public float normalize(float value) {
    return (value - min) / delta;
  }

}