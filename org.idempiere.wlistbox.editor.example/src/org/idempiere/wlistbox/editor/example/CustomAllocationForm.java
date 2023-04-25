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
 *                                                                     *
 * Contributors:                                                       *
 * - hengsin                         								   *
 **********************************************************************/
package org.idempiere.wlistbox.editor.example;

import java.math.BigDecimal;

import org.adempiere.webui.apps.form.WAllocation;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.WListItemRenderer;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.component.WTableColumn;
import org.adempiere.webui.editor.WEditor;
import org.adempiere.webui.editor.WNumberEditor;
import org.compiere.minigrid.IMiniTable;
import org.zkoss.zk.ui.Component;

/**
 * @author hengsin
 */
@org.idempiere.ui.zk.annotation.Form(name = "org.compiere.apps.form.VAllocation")
public class CustomAllocationForm extends WAllocation {

	public CustomAllocationForm() {
	}

	@Override
	public void setInvoiceColumnClass(IMiniTable invoiceTable, boolean isMultiCurrency) {
		super.setInvoiceColumnClass(invoiceTable, isMultiCurrency);
		WListbox invoiceListbox = (WListbox) invoiceTable;
		WListItemRenderer renderer = (WListItemRenderer) invoiceListbox.getItemRenderer();
		WTableColumn amtColumn = renderer.getColumn(4);
		if (amtColumn.getEditorProvider() == null) {
			amtColumn.setEditorProvider(t -> newAmtColumnEditor(t));
		}
	}

	private WEditor newAmtColumnEditor(WTableColumn.EditorProviderParameters parameters) {
		return new WNumberEditor() {
			@Override
			public Component getDisplayComponent() {
				Label label = new Label();
				if (getValue() != null) {
					if (getValue().compareTo(new BigDecimal("100.00")) > 0) {
						label.setStyle("color:red;text-align:right;width:100%;display:inline-block;");
					} else {
						label.setStyle("color:green;text-align:right;width:100%;display:inline-block;");
					}
				}
				return label;
			}
		};
	}
}
