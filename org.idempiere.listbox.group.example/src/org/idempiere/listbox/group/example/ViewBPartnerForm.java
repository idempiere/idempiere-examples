package org.idempiere.listbox.group.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.webui.panel.ADForm;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.idempiere.ui.zk.annotation.Form;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Vlayout;

@Form
public class ViewBPartnerForm extends ADForm {

	private static final long serialVersionUID = 5865090361697678173L;

	private Listbox bpListbox = null;
	
	public ViewBPartnerForm() {
	}

	@Override
	protected void initForm() {
		Vlayout layout = new Vlayout();
		appendChild(layout);
		layout.setHeight("100%");
		layout.setWidth("100%");
		
		bpListbox = new Listbox();
		layout.appendChild(bpListbox);
		bpListbox.setVflex(true);
		bpListbox.setHflex("1");
		
		Listhead listhead = bpListbox.getListhead();
		if (listhead == null) {
			listhead = new Listhead();
			bpListbox.appendChild(listhead);
		} else {
			listhead.getChildren().clear();
		}
		listhead.setSizable(true);		
		
		Listheader header = new Listheader();
		listhead.appendChild(header);
		header.setLabel("Line");
		header.setWidth("55px");
		
		header = new Listheader();
		listhead.appendChild(header);
		header.setLabel(Msg.getElement(Env.getCtx(), "Name"));
		header.setWidth("200px");
		
		header = new Listheader();
		listhead.appendChild(header);
		header.setLabel(Msg.getElement(Env.getCtx(), "Value"));
		header.setWidth("200px");
		
		header = new Listheader();
		listhead.appendChild(header);
		header.setLabel(Msg.getElement(Env.getCtx(), MBPartner.COLUMNNAME_SO_CreditLimit));
		header.setWidth("150px");
		
		header = new Listheader();
		listhead.appendChild(header);
		header.setLabel(Msg.getElement(Env.getCtx(), MBPartner.COLUMNNAME_SO_CreditUsed));
		header.setWidth("150px");
		
		BPartnerGroupRenderer renderer = new BPartnerGroupRenderer();
		bpListbox.setItemRenderer(renderer);
		bpListbox.setModel(createGroupModel());
	}

	protected BPartnerGroupModel createGroupModel() {
		List<List<MBPartner>> groupBPartners = new ArrayList<>();
		Query query = new Query(Env.getCtx(), MBPGroup.Table_Name, null, null);
		List<MBPGroup> groups = query.setClient_ID().setOnlyActiveRecords(true).setOrderBy("Name").list();
		List<Map<String, BigDecimal>> totals = new ArrayList<>(); 
		for(MBPGroup group : groups) {
			query = new Query(Env.getCtx(), MBPartner.Table_Name, MBPartner.Table_Name + "." + MBPartner.COLUMNNAME_C_BP_Group_ID + "=?", null); 
			List<MBPartner> bpartners = query.setParameters(group.getC_BP_Group_ID()).setOnlyActiveRecords(true).setOrderBy("Name").list();
			groupBPartners.add(bpartners);
			Map<String, BigDecimal> total = new HashMap<>();
			total.put(MBPartner.COLUMNNAME_SO_CreditLimit, BigDecimal.ZERO);
			total.put(MBPartner.COLUMNNAME_SO_CreditUsed, BigDecimal.ZERO);
			bpartners.forEach(bp -> {
				total.put(MBPartner.COLUMNNAME_SO_CreditLimit, total.get(MBPartner.COLUMNNAME_SO_CreditLimit).add(bp.getSO_CreditLimit()));
				total.put(MBPartner.COLUMNNAME_SO_CreditUsed, total.get(MBPartner.COLUMNNAME_SO_CreditUsed).add(bp.getSO_CreditUsed()));
			});
			totals.add(total);
		}
		BPartnerGroupModel groupModel = new BPartnerGroupModel(groupBPartners, groups, totals);
		groupModel.setMultiple(true);
		groupModel.setGroupSelectable(true);
		return groupModel;
	}
}
