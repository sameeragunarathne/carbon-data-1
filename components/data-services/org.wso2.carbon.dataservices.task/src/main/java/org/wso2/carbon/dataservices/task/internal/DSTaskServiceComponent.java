/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.dataservices.task.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.dataservices.task.DSTaskConstants;
import org.wso2.carbon.ntask.core.service.TaskService;

@Component(
        name = "dataservices.task",
        immediate = true)
public class DSTaskServiceComponent {

    private static final Log log = LogFactory.getLog(DSTaskServiceComponent.class);

    private static TaskService taskService;

    @Activate
    protected void activate(ComponentContext ctxt) {

        try {
            /* register the data service task type */
            getTaskService().registerTaskType(DSTaskConstants.DATA_SERVICE_TASK_TYPE);
            if (log.isDebugEnabled()) {
                log.debug("Data Services task bundle is activated ");
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            /* don't throw exception */
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {

        log.debug("Data Services task bundle is deactivated ");
    }

    @Reference(
            name = "ntask.component",
            service = org.wso2.carbon.ntask.core.service.TaskService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetTaskService")
    protected void setTaskService(TaskService taskService) {

        if (log.isDebugEnabled()) {
            log.debug("Setting the Task Service");
        }
        DSTaskServiceComponent.taskService = taskService;
    }

    protected void unsetTaskService(TaskService taskService) {

        if (log.isDebugEnabled()) {
            log.debug("Unsetting the Task Service");
        }
        DSTaskServiceComponent.taskService = null;
    }

    public static TaskService getTaskService() {

        return DSTaskServiceComponent.taskService;
    }

}
