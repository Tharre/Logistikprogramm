package org.htl_hl.Logistikprogramm;

import org.jooq.*;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JOOQTableModel<E extends TableRecord> extends AbstractTableModel {

	private Result<?> recordResult;
	private List<ForeignKey<E, ?>> foreignKeys;
	private String[] columnNames;
	private Map<Table<?>, Map<?, ? extends Record>> referencedTables = new HashMap<>();
	private boolean editable = true;

	public JOOQTableModel(LConnection server, String[] columnNames, Result<?> recordResult,
	                      List<ForeignKey<E, ?>> foreignKeys) {
		this.columnNames = columnNames;
		this.recordResult = recordResult;
		this.foreignKeys = foreignKeys;

		if (getColumnCount() != columnNames.length)
			throw new IllegalArgumentException(
					"Record column count doesn't match column names count (" + getColumnCount() + ", " +
					columnNames.length + ")");

		for (ForeignKey fk : foreignKeys) {
			Table<?> referencedTable = fk.getKey().getTable();
			System.out.println("Fetching " + referencedTable);
			if (fk.getKey().getFieldsArray().length != 1)
				continue;

			Field<?> field = fk.getKey().getFieldsArray()[0];
			referencedTables.put(referencedTable, server.create.fetch(referencedTable).intoMap(field));
		}
	}

	public JOOQTableModel(LConnection server, String[] columnNames, Result<?> recordResult, Table<E> baseTable) {
		this(server, columnNames, recordResult, baseTable.getReferences());
	}

	@Override
	public int getRowCount() {
		return recordResult.size();
	}

	@Override
	public int getColumnCount() {
		return recordResult.fields().length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	private Table<?> getReferencedTable(Field field) {
		for (ForeignKey<?, ?> fk : foreignKeys) {
			for (Field<?> keyField : fk.getFields())
				if (keyField.equals(field))
					return fk.getKey().getTable();
		}

		return null;
	}

	@Override
	public Class<?> getColumnClass(int col) {
		Field field = recordResult.field(col);

		if (getReferencedTable(field) != null)
			return Reference.class;

		return field.getType();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = recordResult.getValue(rowIndex, columnIndex);
		if (value != null && getColumnClass(columnIndex) == Reference.class) {
			Field field = recordResult.field(columnIndex);
			Table<?> refTable = getReferencedTable(field);

			return new Reference(refTable, value, referencedTables.get(refTable).get(value).getValue(1).toString());
		}

		return value;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// first column is assumed to be the ID and should never be edited because it risks SQL constraint violations
		return editable && columnIndex != 0 && getColumnClass(columnIndex) != Reference.class;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public void setValueAt(Object o, int rowIndex, int columnIndex) {
		Field field = recordResult.field(columnIndex);
		recordResult.get(rowIndex).setValue(field, o);

		if (recordResult.get(rowIndex).changed()) {
			UpdatableRecord t = (UpdatableRecord) recordResult.get(rowIndex);
			t.store();
		}
	}
}