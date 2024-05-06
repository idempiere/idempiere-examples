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

import java.util.Enumeration;

import org.adempiere.base.annotation.EventTopicDelegate;
import org.adempiere.base.event.LoginEventData;
import org.adempiere.base.event.annotations.AfterLoadPref;
import org.adempiere.base.event.annotations.AfterLoginEventDelegate;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

@EventTopicDelegate
public class MyAfterLoginEventDelegate extends AfterLoginEventDelegate {

	public MyAfterLoginEventDelegate(Event event) {
		super(event);
	}
	
	@Override
	protected void onAfterLogin(LoginEventData data) {
		MUser user = MUser.get(data.getAD_User_ID());
		System.out.println("onAfterLogin: " + user);
	}

	@AfterLoadPref
	public void onAfterLoadPref() {
		Enumeration<Object> e = Env.getCtx().keys();
		StringBuilder builder = new StringBuilder();
		while (e.hasMoreElements()) {
			Object k = e.nextElement();
			if (k != null && k.toString().startsWith("P|")) {
				if (builder.length() > 0)
					builder.append(", ");
				builder.append(k.toString());
			}
		}
		System.out.println("onAfterLoadPref: " + builder.toString());
	}
}
