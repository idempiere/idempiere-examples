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
package org.idempiere.process.annotation.mapped.example;

import java.io.File;
import java.io.FileWriter;

import org.adempiere.base.annotation.Parameter;
import org.adempiere.base.annotation.Process;
import org.compiere.model.MBPartner;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

@Process
public class PrintBusinessPartnerXML extends SvrProcess {

	@Parameter
	private int p_C_BPartner_ID;
	
	public PrintBusinessPartnerXML() {
	}

	@Override
	protected void prepare() {
	}

	@Override
	protected String doIt() throws Exception {
		MBPartner bp = MBPartner.get(Env.getCtx(), p_C_BPartner_ID);
		File exportFile = File.createTempFile(bp.getValue(), ".xml");
		FileWriter writer = new FileWriter(exportFile);
		writer.write(bp.get_xmlString(null).toString());
		writer.close();
		if (processUI != null)
			processUI.download(exportFile);
		return "@Ok@";
	}

}
