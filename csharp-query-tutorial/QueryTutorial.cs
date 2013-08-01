using System;
using System.Collections;
using Araqne.Logdb.Client;

namespace Logpresso.Examples
{
	class QueryTutorial
	{
		[STAThread]
		static void Main(string[] args)
		{
			LogDbClient client = new LogDbClient();
			client.Connect("localhost", 8888, "araqne", "");
			LogCursor cursor = client.Query("logdb tables");
			foreach (IDictionary o in cursor)
			{
				Console.WriteLine(PrettyPrint(o));
			}
		}

		private static string PrettyPrint(IDictionary dict)
		{
			if (dict == null)
				return "";

			string s = "[";
			int i = 0;

			ICollection keys = dict.Keys;
			foreach (object key in keys)
			{
				object val = dict[key];
				s += key.ToString() + "=" + (val == null ? "null" : dict[key].ToString());
				if (i++ != 0)
					s += ", ";
			}
			return s + "]";
		}
	}
}
