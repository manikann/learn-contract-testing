import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    request {
        method HttpMethods.HttpMethod.GET
        url "/reservations"
    }
    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body("""
            [ { "id": "1", "name": "contract-test..." } ]
            """
        )
    }
}