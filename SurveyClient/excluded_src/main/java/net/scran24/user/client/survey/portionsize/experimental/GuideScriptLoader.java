/*
This file is part of Intake24.

Copyright 2015, 2016 Newcastle University.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This file is based on Intake24 v1.0.

© Crown copyright, 2012, 2013, 2014

Licensed under the Open Government Licence 3.0: 

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package net.scran24.user.client.survey.portionsize.experimental;

import org.pcollections.PMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.scran24.user.client.services.FoodLookupServiceAsync;
import net.scran24.user.shared.lookup.GuideDef;

public class GuideScriptLoader implements PortionSizeScriptLoader {
  private final FoodLookupServiceAsync lookupService = FoodLookupServiceAsync.Util.getInstance();

  private final String currentLocale = "en_GB"; // LocaleInfo.getCurrentLocale().getLocaleName()

  @Override
  public void loadResources(final PMap<String, String> data, final AsyncCallback<PortionSizeScript> onComplete) {
    lookupService.getGuideDef(data.get("guide-image-id"), currentLocale, new AsyncCallback<GuideDef>() {
      @Override
      public void onFailure(Throwable caught) {
        onComplete.onFailure(caught);
      }

      @Override
      public void onSuccess(final GuideDef guideDef) {
        onComplete.onSuccess(new GuideScript(guideDef));
      }
    });
  }
}