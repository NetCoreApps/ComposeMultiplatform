using ServiceStack;
using MyApp.ServiceModel;

namespace MyApp.ServiceInterface;

public class SearchFilesService : Service
{
    public object Any(SearchFiles request)
    {
        var files = VirtualFiles.GetAllMatchingFiles(request.Pattern ?? "*");
        return new SearchFilesResponse {
            Results = files.Map(x => x.VirtualPath)
        };
    }
}