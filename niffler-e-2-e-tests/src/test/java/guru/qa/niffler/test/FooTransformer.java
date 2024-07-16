package guru.qa.niffler.test;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class FooTransformer extends ResponseDefinitionTransformer {

    @Override
    public String getName() {
        String s = null;
        return "foo-transformer";


    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        String queryParam = request.queryParameter("id").firstValue();

        String responseBody = "123".equals(queryParam)
                ? """
                {
                    "id": "123",
                    "orderName": "foo",
                }
                """
                : """
                {
                    "id": "777",
                    "orderName": "bar",
                }
                """;

        return ResponseDefinitionBuilder.like(responseDefinition)
                .but()
                .withBody(responseBody)
                .build();
    }
}
