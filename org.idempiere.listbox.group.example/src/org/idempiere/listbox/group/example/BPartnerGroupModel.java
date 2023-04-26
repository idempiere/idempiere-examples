package org.idempiere.listbox.group.example;

import java.util.List;

import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.zkoss.zul.SimpleGroupsModel;

public class BPartnerGroupModel extends SimpleGroupsModel<MBPartner, MBPGroup, Object, Object> {

	private static final long serialVersionUID = -293018786241123038L;
	
	public BPartnerGroupModel(List<List<MBPartner>> data, List<MBPGroup> heads, List<Object> foots) {
		super(data, heads, foots);
	}
}
