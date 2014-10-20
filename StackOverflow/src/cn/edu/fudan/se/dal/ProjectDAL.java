package cn.edu.fudan.se.dal;

import java.sql.ResultSet;

import cn.edu.fudan.se.util.Language;

public class ProjectDAL
{
	public static Language getProjectLanguage()
	{
		Language result = null;
		String sql = "SELECT language  FROM " + DBHelper.PROJECT_LANGUAGE
				+ " where projectName ='" + DBHelper.getCurrentProjectName()
				+ "' ";
		ResultSet rs = DBHelper.executeQuery(sql);
		try
		{
			if (rs.next())
			{
				result = Language.valueOf(rs.getString(1));
			}

		}
		catch (Exception exp)
		{
			exp.printStackTrace();
		}
		return result;
	}

	public static void insertProjectLanguage(Language language)
	{
		clearProjectData(DBHelper.getCurrentProjectName(),
				DBHelper.PROJECT_LANGUAGE);
		String sql = "insert into " + DBHelper.PROJECT_LANGUAGE
				+ " (projectname, language) values ('"
				+ DBHelper.getCurrentProjectName() + "','"
				+ language.toString() + "')";
		DBHelper.executeUpdate(sql);

	}

	protected static void clearProjectData(String projectName, String tableName)
	{
		String sql = "delete  from  " + tableName + "  where projectName = '"
				+ projectName + "'";
		DBHelper.executeUpdate(sql);
	}

}
