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
import org.adempiere.base.event.annotations.doc.AfterReverseAccrual;
import org.adempiere.base.event.annotations.doc.AfterReverseCorrect;
import org.adempiere.base.event.annotations.doc.BeforeReverseAccrual;
import org.adempiere.base.event.annotations.doc.BeforeReverseCorrect;
import org.compiere.model.MInvoice;
import org.osgi.service.event.Event;

@EventTopicDelegate
@ModelEventTopic(modelClass = MInvoice.class)
public class MyInvoiceDocumentEventDelegate extends ModelEventDelegate<MInvoice> {

	public MyInvoiceDocumentEventDelegate(MInvoice po, Event event) {
		super(po, event);
	}

	@BeforeReverseAccrual
	public void onBeforeReverseAccrual() {
		System.out.println("onBeforeReverseAccrual: " + getModel().get_xmlString(null));
	}
	
	@AfterReverseAccrual
	public void onAfterReverseAccrual() {
		System.out.println("onAfterReverseAccrual: " + getModel().get_xmlString(null));
	}
	
	@BeforeReverseCorrect
	public void onBeforeReverseCorrect() {
		System.out.println("onBeforeReverseCorrect: " + getModel().get_xmlString(null));
	}
	
	@AfterReverseCorrect
	public void onAfterReverseCorrect() {
		System.out.println("onAfterReverseCorrect: " + getModel().get_xmlString(null));
	}
}
