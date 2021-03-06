/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.http.core.loadbalancer;

import io.gravitee.definition.model.Api;
import io.gravitee.definition.model.Endpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 * @author GraviteeSource Team
 */
public abstract class WeightedLoadBalancerStrategy extends LoadBalancerSupportStrategy {

    transient int lastIndex;

    private List<WeightRatio> runtimeRatios = new ArrayList<>();

    public WeightedLoadBalancerStrategy(Api api) {
        super(api);
        loadRuntimeRatios();
    }

    protected void loadRuntimeRatios() {
        int position = 0;

        for(Endpoint endpoint : availableEndpoints()) {
            runtimeRatios.add(new WeightRatio(position++, endpoint.getWeight()));
        }
    }

    protected boolean isRuntimeRatiosZeroed() {
        boolean cleared = true;

        for (WeightRatio runtimeRatio : runtimeRatios) {
            if (runtimeRatio.getRuntime() > 0) {
                cleared = false;
            }
        }
        return cleared;
    }

    protected void resetRuntimeRatios() {
        for (WeightRatio runtimeRatio : runtimeRatios) {
            runtimeRatio.setRuntime(runtimeRatio.getDistribution());
        }
    }

    public List<WeightRatio> getRuntimeRatios() {
        return runtimeRatios;
    }
}
