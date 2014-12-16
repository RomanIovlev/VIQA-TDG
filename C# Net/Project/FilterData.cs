using System.Linq;

namespace Testing.ValuesGenerator
{
    public class FilterData
    {
        public string[] WhiteList;
        public string[] BlackList;
        public string[] AvailableGroups;
        public string Breadcrumb = "";

        public FilterData() { }

        public FilterData(string[] whiteList, string[] blackList, string[] availableGroups, string breadcrumb)
        {
            WhiteList = whiteList;
            BlackList = blackList;
            AvailableGroups = availableGroups;
            Breadcrumb = breadcrumb;
        }

        public FilterData(FilterData filterData, string fieldName) : 
            this(filterData.WhiteList, filterData.BlackList, filterData.AvailableGroups, filterData.Breadcrumb + "." + fieldName) { }

        public string GetBreadCrumbForCheck()
        {
            return (Breadcrumb.Last() == '.')
                ? Breadcrumb
                : Breadcrumb + ".";
        }

        public string GetCheckValue(string value)
        {
            if (value.First() != '.')
                value = "." + value;
            if (value.Last() != '.')
                value = value + ".";
            return value;
        }

        public bool AllowGeneration()
        {
            if (WhiteList != null && WhiteList.Any())
                return WhiteList.Any(val => GetBreadCrumbForCheck().Contains(GetCheckValue(val)));
            if (BlackList != null && BlackList.Any())
                return !BlackList.Any(val => GetBreadCrumbForCheck().Contains(GetCheckValue(val)));
            return true;
        }
    }
}