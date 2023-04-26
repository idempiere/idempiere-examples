package org.idempiere.listbox.group.example;

import java.math.BigDecimal;
import java.util.Map;

import org.adempiere.webui.component.Label;
import org.adempiere.webui.listbox.renderer.AbstractGroupListitemRenderer;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.util.DisplayType;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class BPartnerGroupRenderer extends AbstractGroupListitemRenderer<Object> {

	private int groupItemIndex = 0;
	
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getGroupHeaderTitle(Object data) {	
		MBPGroup group = (MBPGroup) data;
		groupItemIndex = 0;
		return group.getName();
	}

	@Override
	public void renderGroupfoot(Listitem item, Object data, int index) {
		@SuppressWarnings("unchecked")
		Map<String, BigDecimal> total = (Map<String, BigDecimal>) data;
		Listcell cell = new Listcell();
		cell.setSpan(3);
		cell.appendChild(new Label("Total:").rightAlign());
		item.appendChild(cell);
		
		cell = new Listcell();
		cell.appendChild(new Label(DisplayType.getNumberFormat(DisplayType.Amount).format(total.get(MBPartner.COLUMNNAME_SO_CreditLimit))).rightAlign());
		item.appendChild(cell);		
		
		cell = new Listcell();
		cell.appendChild(new Label(DisplayType.getNumberFormat(DisplayType.Amount).format(total.get(MBPartner.COLUMNNAME_SO_CreditUsed))).rightAlign());
		item.appendChild(cell);
	}

	@Override
	public void renderListitem(Listitem item, Object data, int index) {
		MBPartner bp = (MBPartner) data;
		
		Listcell cell = new Listcell();
		cell.appendChild(new Label(Integer.toString(++groupItemIndex)));
		item.appendChild(cell);
		
		cell = new Listcell();
		cell.appendChild(new Label(bp.getName()));
		item.appendChild(cell);
		
		cell = new Listcell();
		cell.appendChild(new Label(bp.getValue()));
		item.appendChild(cell);		
		
		cell = new Listcell();
		cell.appendChild(new Label(DisplayType.getNumberFormat(DisplayType.Amount).format(bp.getSO_CreditLimit())).rightAlign());
		item.appendChild(cell);		
		
		cell = new Listcell();
		cell.appendChild(new Label(DisplayType.getNumberFormat(DisplayType.Amount).format(bp.getSO_CreditUsed())).rightAlign());
		item.appendChild(cell);		
	}

}
