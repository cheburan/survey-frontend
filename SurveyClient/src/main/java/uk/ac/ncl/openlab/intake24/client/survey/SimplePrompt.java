/*
This file is part of Intake24.

© Crown copyright, 2012, 2013, 2014.

This software is licensed under the Open Government Licence 3.0:

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package uk.ac.ncl.openlab.intake24.client.survey;

import org.workcraft.gwt.shared.client.Callback1;

import com.google.gwt.user.client.ui.FlowPanel;

public interface SimplePrompt<T> {
    public FlowPanel getInterface(final Callback1<T> onComplete);

    public String getClassName();
}
