/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 **********************************************************************/
package org.idempiere.event.annotation.example.delegates;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.annotation.ModelEventTopic;
import org.adempiere.base.event.annotations.ModelEventDelegate;
import org.adempiere.base.event.annotations.po.AfterChange;
import org.adempiere.base.event.annotations.po.AfterDelete;
import org.adempiere.base.event.annotations.po.AfterNew;
import org.adempiere.base.event.annotations.po.BeforeChange;
import org.adempiere.base.event.annotations.po.BeforeDelete;
import org.adempiere.base.event.annotations.po.BeforeNew;
import org.adempiere.base.event.annotations.po.PostCreate;
import org.adempiere.base.event.annotations.po.PostDelete;
import org.adempiere.base.event.annotations.po.PostUpdate;
import org.compiere.model.MTest;
import org.osgi.service.event.Event;

@EventTopicDelegate
@ModelEventTopic(modelClass = MTest.class)
public class MyTestModelEventDelegate extends ModelEventDelegate<MTest> {

	public MyTestModelEventDelegate(MTest po, Event event) {
		super(po, event);
	}

	@BeforeChange
	public void onBeforeChange() {
		System.out.println("onBeforeChange: " + getModel().get_xmlString(null).toString());
	}
	
	@AfterChange
	public void onAfterChange() {
		System.out.println("onAfterChange: " + getModel().get_xmlString(null).toString());
	}
	
	@BeforeNew
	public void onBeforeNew() {
		System.out.println("onBeforeNew: " + getModel().get_xmlString(null).toString());
	}
	
	@AfterNew
	public void onAfterNew() {
		System.out.println("onAfterNew: " + getModel().get_xmlString(null).toString());
	}
	
	@BeforeDelete
	public void onBeforeDelete() {
		System.out.println("onBeforeDelete: " + getModel().get_xmlString(null).toString());
	}
	
	@AfterDelete
	public void onAfterDelete() {
		System.out.println("onAfterDelete: " + getModel().get_xmlString(null).toString());
	}
	
	@PostCreate
	public void onPostCreate() {
		System.out.println("onPostCreate: " + getModel().get_xmlString(null).toString());
	}
	
	@PostUpdate
	public void onPostUpdate() {
		System.out.println("onPostUpdate: " + getModel().get_xmlString(null).toString());
	}
	
	@PostDelete
	public void onPostDelete() {
		System.out.println("onPostDelete: " + getModel().get_xmlString(null).toString());
	}
}
