/*
 * Copyright 2014 Attila Szegedi, Daniel Dekany, Jonathan Revusky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package freemarker.core;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateOutputModel;

class BuiltInsForOutputFormatRelated {

    static class no_escBI extends AbstractConverterBI {

        @Override
        protected TemplateModel calculateResult(String lho, OutputFormat outputFormat, Environment env)
                throws TemplateException {
            return outputFormat.fromMarkup(lho);
        }
        
    }

    static class escBI extends AbstractConverterBI {

        @Override
        protected TemplateModel calculateResult(String lho, OutputFormat outputFormat, Environment env)
                throws TemplateException {
            return outputFormat.escapePlainText(lho);
        }
        
    }
    
    static abstract class AbstractConverterBI extends BuiltInForOutputFormatRelated {

        @Override
        protected TemplateModel calculateResult(OutputFormat contextOF, Environment env) throws TemplateException {
            TemplateModel lhoTM = target.eval(env);
            String lhoStr = EvalUtil.coerceModelToString(lhoTM, target, null, true, env);
            if (lhoStr == null) { // should indicate that lhoTM is a TOM
                TemplateOutputModel lhoTOM;
                try {
                    lhoTOM = (TemplateOutputModel) lhoTM;
                } catch (ClassCastException e) {
                    throw EvalUtil.newModelHasStoredNullException(null, lhoTM, target);
                }
                OutputFormat lhoOF = lhoTOM.getOutputFormat();
                if (lhoOF == contextOF) {
                    // bypass
                    return lhoTM;
                } else {
                    lhoStr = lhoOF.getSourcePlainText(lhoTOM);
                    if (lhoStr == null) {
                        throw new _TemplateModelException(target,
                                "The left side operand of ?", key, " was a template output fragment of format ",
                                new _DelayedToString(lhoOF),
                                ", which differs from the current output format, ",
                                new _DelayedToString(contextOF),
                                ". Also it has no source plain text to re-apply the current output format on it.");
                    }
                    // Here we know that lho is escaped plain text. So we re-escape it to the current format and
                    // bypass it, just as if the two output formats were the same earlier.
                    return contextOF.escapePlainText(lhoStr);
                }
            }
            return calculateResult(lhoStr, contextOF, env);
        }
        
        protected abstract TemplateModel calculateResult(String lho, OutputFormat outputFormat, Environment env)
                throws TemplateException;
        
    }
    
}