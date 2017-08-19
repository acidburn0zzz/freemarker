/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.freemarker.core.templatesuite.models;

import org.apache.freemarker.core.TemplateException;
import org.apache.freemarker.core.model.ObjectWrapper;
import org.apache.freemarker.core.model.TemplateHashModel;
import org.apache.freemarker.core.model.TemplateModel;
import org.apache.freemarker.core.model.TemplateSequenceModel;
import org.apache.freemarker.core.model.impl.SimpleScalar;
import org.apache.freemarker.core.model.impl.SimpleSequence;

/**
 * Testcase to see how FreeMarker deals with multiple Template models.
 */
public class MultiModel4 implements TemplateSequenceModel, TemplateHashModel {

    private final SimpleSequence m_cList;

    public MultiModel4(ObjectWrapper ow) {
        this.m_cList = new SimpleSequence(ow);
    }

    /**
     * @return the specified index in the list
     */
    @Override
    public TemplateModel get(int i) throws TemplateException {
        return m_cList.get( i );
    }

    /**
     * Gets a <tt>TemplateModel</tt> from the hash.
     *
     * @param key the name by which the <tt>TemplateModel</tt>
     * is identified in the template.
     * @return the <tt>TemplateModel</tt> referred to by the key,
     * or null if not found.
     */
    @Override
    public TemplateModel get(String key) {
        if ( key.equals( "size" )) {
            return new SimpleScalar( "Key size, not the listSize method." );
        } else {
            return null;
        }
    }


    @Override
    public int size() {
        return m_cList.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

}