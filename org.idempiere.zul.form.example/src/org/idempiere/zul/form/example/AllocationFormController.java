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
package org.idempiere.zul.form.example;

import static org.adempiere.webui.ClientInfo.MEDIUM_WIDTH;
import static org.adempiere.webui.ClientInfo.SMALL_WIDTH;
import static org.adempiere.webui.ClientInfo.maxWidth;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_BPARTNER_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_CURRENCY_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_PERIOD_AD_ORG_ID;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.DocumentLink;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.Dialog;
import org.compiere.apps.form.Allocation;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.compiere.util.Util;
import org.idempiere.ui.zk.annotation.Form;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;

@Form
public class AllocationFormController extends Allocation implements IFormController, ValueChangeListener, WTableModelListener {

	private AllocationForm allocationForm;
	private int noOfColumn;

	/** Currency parameter */
	private WTableDirEditor currencyPick = null;
	/** Organization parameter */
	private WTableDirEditor organizationPick;
	/** bpartner parameter */
	private WSearchEditor bpartnerSearch = null;
	/** Document date parameter */
	private WDateEditor dateField = new WDateEditor();
	/** Charges. Part of {@link #allocationLayout}. */
	private WTableDirEditor chargePick = null;
	/** Document types. Part of {@link #allocationLayout}. */
	private WTableDirEditor docTypePick = null;
	
	public AllocationFormController() {
		allocationForm = new AllocationForm();
		Selectors.wireEventListeners(allocationForm, this);
		Selectors.wireEventListeners(allocationForm.parameterLayoutComponent, this);
		Selectors.wireEventListeners(allocationForm.allocationLayoutComponent, this);
		try {
			super.dynInit();
			initializeEditors();
			initializeComponents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ADForm getForm() {
		return allocationForm;
	}

	/**
	 *  Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initializeEditors() throws Exception
	{
		//  Currency
		int AD_Column_ID = COLUMN_C_INVOICE_C_CURRENCY_ID;    //  C_Invoice.C_Currency_ID
		MLookup lookupCur = MLookupFactory.get (Env.getCtx(), allocationForm.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		currencyPick = new WTableDirEditor("C_Currency_ID", true, false, true, lookupCur);
		currencyPick.setValue(getC_Currency_ID());
		currencyPick.addValueChangeListener(this);

		// Organization filter selection
		AD_Column_ID = COLUMN_C_PERIOD_AD_ORG_ID; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), allocationForm.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addValueChangeListener(this);
		
		//  BPartner
		AD_Column_ID = COLUMN_C_INVOICE_C_BPARTNER_ID;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), allocationForm.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBP);
		bpartnerSearch.addValueChangeListener(this);

		//  Default dateField to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(Env.getCtx(), Env.DATE));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateField.setValue(new Timestamp(cal.getTimeInMillis()));
		dateField.addValueChangeListener(this);

		//  Charge
		AD_Column_ID = 61804;    //  C_AllocationLine.C_Charge_ID
		MLookup lookupCharge = MLookupFactory.get (Env.getCtx(), allocationForm.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		chargePick = new WTableDirEditor("C_Charge_ID", false, false, true, lookupCharge);
		chargePick.setValue(getC_Charge_ID());
		chargePick.addValueChangeListener(this);
		
		//  Doc Type
		AD_Column_ID = 212213;    //  C_AllocationLine.C_DocType_ID
		MLookup lookupDocType = MLookupFactory.get (Env.getCtx(), allocationForm.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		docTypePick = new WTableDirEditor("C_DocType_ID", false, false, true, lookupDocType);
		docTypePick.setValue(getC_DocType_ID());
		docTypePick.addValueChangeListener(this);			
	}   //  dynInit
	
	protected void initializeComponents() {		
			
		allocationForm.autoWriteOff.setSelected(false);
		
		allocationForm.invoiceInfo.setText(".");
		allocationForm.paymentInfo.setText(".");
		
		allocationForm.differenceField.setText("0");
		allocationForm.differenceField.setReadonly(true);
		allocationForm.differenceField.setStyle("text-align: right");
		
		allocationForm.allocateButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Process")));		
		
		layoutParameterAndSummary();
	}

	protected void layoutParameterAndSummary() {
		setupParameterColumns();
		
		// Status bar
		allocationForm.statusBar.appendChild(new Label(Msg.getMsg(Env.getCtx(), "AllocateStatus")));
				
		ZKUpdateUtil.setHflex(bpartnerSearch.getComponent(), "true");
		allocationForm.bpartnerSearchCell.appendChild(bpartnerSearch.getComponent());
		bpartnerSearch.showMenu();
		
		allocationForm.dateFieldCell.appendChild(dateField.getComponent());
		
		ZKUpdateUtil.setHflex(organizationPick.getComponent(), "true");
		allocationForm.organizationPickCell.appendChild(organizationPick.getComponent());
		organizationPick.showMenu();
		
		ZKUpdateUtil.setHflex(currencyPick.getComponent(), "true");
		allocationForm.currencyPickCell.appendChild(currencyPick.getComponent());		
		currencyPick.showMenu();
		
		if (noOfColumn < 6)		
			LayoutUtils.compactTo(allocationForm.parameterLayout, noOfColumn);
		else
			LayoutUtils.expandTo(allocationForm.parameterLayout, noOfColumn, true);
		
		ZKUpdateUtil.setHflex(chargePick.getComponent(), "true");
		allocationForm.chargePickCell.appendChild(chargePick.getComponent());
		
		ZKUpdateUtil.setHflex(docTypePick.getComponent(), "true");
		allocationForm.docTypePickCell.appendChild(docTypePick.getComponent());
		docTypePick.showMenu();
	}
	
	protected void reLayoutParameterAndSummary() {
		allocationForm.parameterLayoutPanel.getChildren().clear();
		allocationForm.allocationLayoutPanel.getChildren().clear();
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
			allocationForm.parameterLayoutComponent = Executions.createComponents("~./parameter-layout.zul", allocationForm.parameterLayoutPanel, allocationForm.arguments);
			if (maxWidth(SMALL_WIDTH-1)) {
				allocationForm.allocationLayoutComponent = Executions.createComponents("~./allocation-layout-sw.zul", allocationForm.allocationLayoutPanel, allocationForm.arguments);
			} else {
				allocationForm.allocationLayoutComponent = Executions.createComponents("~./allocation-layout.zul", allocationForm.allocationLayoutPanel, allocationForm.arguments);
			}
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
		
		allocationForm.parameterLayout = null;
		allocationForm.multiCurrency = null;
		allocationForm.autoWriteOff = null;
		allocationForm.currencyPickCell = null;
		Selectors.wireComponents(allocationForm.parameterLayoutComponent, allocationForm, true);
		allocationForm.allocationLayout = null;
		allocationForm.chargePickCell = null;
		allocationForm.docTypePickCell = null;
		allocationForm.statusBar = null;
		allocationForm.differenceField = null;
		allocationForm.allocateButton = null;
		allocationForm.refreshButton = null;
		allocationForm.allocCurrencyLabel = null;
		Selectors.wireComponents(allocationForm.allocationLayoutComponent, allocationForm, true);
		Selectors.wireEventListeners(allocationForm.parameterLayoutComponent, this);
		Selectors.wireEventListeners(allocationForm.allocationLayoutComponent, this);
		
		setupParameterColumns();
		
		allocationForm.statusBar.appendChild(new Label(Msg.getMsg(Env.getCtx(), "AllocateStatus")));
		
		allocationForm.bpartnerSearchCell.appendChild(bpartnerSearch.getComponent());
		
		allocationForm.dateFieldCell.appendChild(dateField.getComponent());
		
		allocationForm.organizationPickCell.appendChild(organizationPick.getComponent());
		
		allocationForm.currencyPickCell.appendChild(currencyPick.getComponent());		
		
		if (noOfColumn < 6)		
			LayoutUtils.compactTo(allocationForm.parameterLayout, noOfColumn);
		else
			LayoutUtils.expandTo(allocationForm.parameterLayout, noOfColumn, true);
		
		allocationForm.chargePickCell.appendChild(chargePick.getComponent());
		
		allocationForm.docTypePickCell.appendChild(docTypePick.getComponent());
		docTypePick.showMenu();
		
		allocationForm.invalidate();
	}
	
	@Listen("onClick=#allocateButton")
	public void onAllocateButton() {
		allocationForm.allocateButton.setEnabled(false);
		MAllocationHdr allocation = saveData();
		loadBPartner();
		allocationForm.allocateButton.setEnabled(true);
		if (allocation != null) 
		{
			DocumentLink link = new DocumentLink(Msg.getElement(Env.getCtx(), MAllocationHdr.COLUMNNAME_C_AllocationHdr_ID) + ": " + allocation.getDocumentNo(), allocation.get_Table_ID(), allocation.get_ID());				
			allocationForm.statusBar.appendChild(link);
		}
	}
	
	@Listen("onCheck=#multiCurrency")
	public void onMultiCurrency() {
		loadBPartner();
	}
	
	@Listen("onClick=#refreshButton")
	public void onRefreshButton() {
		loadBPartner();
	}
	
	/**
	 * Handle onClientInfo event from browser.
	 */
	protected void onClientInfo()
	{
		if (ClientInfo.isMobile() && allocationForm.getPage() != null) 
		{
			if (noOfColumn > 0 && allocationForm.parameterLayout.getRows() != null)
			{
				int t = 6;
				if (maxWidth(MEDIUM_WIDTH-1))
				{
					if (maxWidth(SMALL_WIDTH-1))
						t = 2;
					else
						t = 4;
				}
				if (t != noOfColumn)
				{
					reLayoutParameterAndSummary();
				}
			}
		}
	}
	
	/**
	 * Save Data to C_AllocationHdr and C_AllocationLine.
	 */
	private MAllocationHdr saveData()
	{
		if (getAD_Org_ID() > 0)
			Env.setContext(Env.getCtx(), allocationForm.getWindowNo(), "AD_Org_ID", getAD_Org_ID());
		else
			Env.setContext(Env.getCtx(), allocationForm.getWindowNo(), "AD_Org_ID", "");
		try
		{
			final MAllocationHdr[] allocation = new MAllocationHdr[1];
			Trx.run(new TrxRunnable() 
			{
				public void run(String trxName)
				{
					allocationForm.statusBar.getChildren().clear();
					allocation[0] = saveData(allocationForm.getWindowNo(), dateField.getValue(), allocationForm.paymentTable, allocationForm.invoiceTable, trxName);
					
				}
			});
			
			return allocation[0];
		}
		catch (Exception e)
		{
			Dialog.error(allocationForm.getWindowNo(), "Error", e.getLocalizedMessage());
			return null;
		}
	}   //  saveData
	
	/**
	 *  Load Business Partner Info.
	 *  <ul>
	 *  <li>Payments</li>
	 *  <li>Invoices</li>
	 *  </ul>
	 */
	private void loadBPartner ()
	{
		checkBPartner();
		
		Vector<Vector<Object>> data = getPaymentData(allocationForm.multiCurrency.isSelected(), dateField.getValue(), (String)null);
		Vector<String> columnNames = getPaymentColumnNames(allocationForm.multiCurrency.isSelected());
		
		allocationForm.paymentTable.clear();
		
		//  Remove previous listeners
		allocationForm.paymentTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelP = new ListModelTable(data);
		modelP.addTableModelListener(this);
		allocationForm.paymentTable.setData(modelP, columnNames);
		setPaymentColumnClass(allocationForm.paymentTable, allocationForm.multiCurrency.isSelected());
		//

		data = getInvoiceData(allocationForm.multiCurrency.isSelected(), dateField.getValue(), (String)null);
		columnNames = getInvoiceColumnNames(allocationForm.multiCurrency.isSelected());
		
		allocationForm.invoiceTable.clear();
		
		//  Remove previous listeners
		allocationForm.invoiceTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelI = new ListModelTable(data);
		modelI.addTableModelListener(this);
		allocationForm.invoiceTable.setData(modelI, columnNames);
		setInvoiceColumnClass(allocationForm.invoiceTable, allocationForm.multiCurrency.isSelected());
		//
		
		//  Calculate Totals
		calculate();
		
		allocationForm.statusBar.getChildren().clear();
	}   //  loadBPartner
	
	/**
	 * perform allocation calculation
	 */
	public void calculate()
	{
		calculate(allocationForm.paymentTable, allocationForm.invoiceTable, allocationForm.multiCurrency.isSelected());
		
		allocationForm.paymentInfo.setText(getPaymentInfoText());
		allocationForm.invoiceInfo.setText(getInvoiceInfoText());
		allocationForm.differenceField.setText(format.format(getTotalDifference()));
		
		//	Set AllocationDate
		if (allocDate != null) {
			if (! allocDate.equals(dateField.getValue())) {
                Clients.showNotification(Msg.getMsg(Env.getCtx(), "AllocationDateUpdated"), Clients.NOTIFICATION_TYPE_INFO, dateField.getComponent(), "start_before", -1, false);       
                dateField.setValue(allocDate);
			}
		}

		//  Set Allocation Currency
		allocationForm.allocCurrencyLabel.setText(currencyPick.getDisplay());				

		setAllocateButton();
	}
	
	/**
	 * Set {@link #allocateButton} to enable or disable.
	 */
	private void setAllocateButton() {
		if (isOkToAllocate() )
		{
			allocationForm.allocateButton.setEnabled(true);
		}
		else
		{
			allocationForm.allocateButton.setEnabled(false);
		}

		if ( getTotalDifference().signum() == 0 )
		{
			chargePick.setValue(null);
			setC_Charge_ID(0);
   		}
	}
	
	/**
	 * Setup columns for {@link #parameterLayout}.
	 */
	protected void setupParameterColumns() {
		noOfColumn = 6;
		if (maxWidth(MEDIUM_WIDTH-1))
		{
			if (maxWidth(SMALL_WIDTH-1))
				noOfColumn = 2;
			else
				noOfColumn = 4;
		}
		if (noOfColumn == 2)
		{
			Columns columns = new Columns();
			Column column = new Column();
			column.setWidth("35%");
			columns.appendChild(column);
			column = new Column();
			column.setWidth("65%");
			columns.appendChild(column);
			allocationForm.parameterLayout.appendChild(columns);
		}
	}

	/**
	 *  Value change listener for parameter and allocation fields.
	 *  @param e event
	 */
	@Override
	public void valueChange (ValueChangeEvent e)
	{
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		if (value == null && (!name.equals("C_Charge_ID")||!name.equals("C_DocType_ID") ))
			return;
		
		// Organization
		if (name.equals("AD_Org_ID"))
		{
			setAD_Org_ID((int) value);
			
			loadBPartner();
		}
		//		Charge
		else if (name.equals("C_Charge_ID") )
		{
			setC_Charge_ID(value!=null? ((Integer) value).intValue() : 0);
			
			setAllocateButton();
		}

		else if (name.equals("C_DocType_ID") )
		{
			setC_DocType_ID(value!=null? ((Integer) value).intValue() : 0);			
		}

		//  BPartner
		if (name.equals("C_BPartner_ID"))
		{
			bpartnerSearch.setValue(value);
			setC_BPartner_ID((int) value);
			loadBPartner();
		}
		//	Currency
		else if (name.equals("C_Currency_ID"))
		{
			setC_Currency_ID((int) value);
			loadBPartner();
		}
		//	Date for Multi-Currency
		else if (name.equals("Date") && allocationForm.multiCurrency.isSelected())
			loadBPartner();
	}   //  vetoableChange

	/**
	 *  Table Model Listener for {@link #paymentTable} and {@link #invoiceTable}
	 *  - Recalculate Totals
	 *  @param e event
	 */
	@Override
	public void tableChanged(WTableModelEvent e)
	{
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);
		//  Not a table update
		if (!isUpdate)
		{
			calculate();
			return;
		}
		
		int row = e.getFirstRow();
		int col = e.getColumn();
	
		if (row < 0)
			return;
		
		boolean isInvoice = (e.getModel().equals(allocationForm.invoiceTable.getModel()));
		boolean isAutoWriteOff = allocationForm.autoWriteOff.isSelected();
		
		String msg = writeOff(row, col, isInvoice, allocationForm.paymentTable, allocationForm.invoiceTable, isAutoWriteOff);
		
		//render row
		ListModelTable model = isInvoice ? allocationForm.invoiceTable.getModel() : allocationForm.paymentTable.getModel(); 
		model.updateComponent(row);
	    
		if(msg != null && msg.length() > 0)
			Dialog.warn(allocationForm.getWindowNo(), "AllocationWriteOffWarn");
		
		calculate();
	}   //  tableChanged
}
