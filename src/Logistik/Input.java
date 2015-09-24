package Logistik;

import javax.swing.*;

import javax.swing.text.*;

import java.sql.*;

/**
 * 
 * Einfaches Eingabefeld
 * 
 * Es kann der Datentyp und eventuelle Überprüfungsmerkmale festgelegt werden
 */

public class Input extends JTextField

{

	private int type;

	public final static int ALL = 0;

	public final static int TEXT = 1;

	public final static int NUMBER = 2;

	private String name;

	private char[] mustHave;

	private boolean key;

	private String canHave;

	public Input(int length, String name)

	{

		this(length, -1, 0, name);

	}

	public Input(int length, int maxlength, int type, String name)

	{

		super(length);

		this.name = name;

		this.type = type;

		if (type == NUMBER) {
			setDocument(createNumericDocumentWithMaxLength(maxlength));
		}

	}

	public void clear()

	{

		setText("");

	}

	public boolean check(DBConnection con, String table)

	{

		if (mustHave != null) {
			for (int i = 0; i < mustHave.length; i++)

			{

				if (contains(mustHave[i]) == false)

				{

					new MessageError("Bitte alle Felder gültig ausfüllen!");

					return false;

				}

			}
		}

		if (getText().equals(""))

		{

			new MessageError("Bitte alle Felder ausfüllen!");

			return false;

		}

		if (key)

		{

			if (!getText().equals(canHave))

			{

				String qry = "SELECT * FROM " + table + " WHERE " + name;

				if (type == NUMBER) {
					qry += "=" + getValue() + ";";
				} else {
					qry += " LIKE " + getValue() + ";";
				}

				ResultSet rs = con.mysql_query(qry);

				try

				{

					if (rs.next())

					{

						new MessageError(
								"Es besteht bereits ein Datensatz mit diesem/dieser "
										+ name);

						rs.close();

						return false;

					}

					rs.close();

				} catch (Exception e) {
					e.getMessage();
				}

			}

		}

		return true;

	}

	public boolean contains(char c)

	{

		String s = getText();

		for (int i = 0; i < s.length(); i++)

		{

			if (s.charAt(i) == c) {
				return true;
			}

		}

		return false;

	}

	@Override
	public String getName()

	{

		return name;

	}

	public String getValue()

	{

		if (type == ALL || type == TEXT) {
			return "'" + getText() + "'";
		}

		return getText();

	}

	public void mustHave(char[] c)

	{

		mustHave = c;

	}

	public void canHave(String c)

	{

		canHave = c;

	}

	public void setKey(boolean b)

	{

		key = b;

	}

	private Document createNumericDocumentWithMaxLength(final int maxLength)

	{

		Document doc = new PlainDocument() {

			@Override
			public void insertString(int offs, String str, AttributeSet a)

			{

				try

				{

					if (getLength() + str.length() > maxLength) {
						return;
					}

					if (str.length() == 1 && !str.matches("[0123456789.]")) {
						return;
					}

					super.insertString(offs, str, a);

				} catch (Exception e) {
				}

			}

		};

		return doc;

	}

}
