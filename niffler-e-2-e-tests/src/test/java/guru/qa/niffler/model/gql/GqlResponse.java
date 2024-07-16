package guru.qa.niffler.model.gql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class GqlResponse<T extends GqlResponse<?>> {
    protected T data;
    protected List<GqlError> errors;
}
