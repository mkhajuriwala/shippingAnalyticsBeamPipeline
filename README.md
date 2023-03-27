# shippingAnalyticsBeamPipeline
Beam pipeline to stream shipping details from pubsub to BigQuery

# Run from command line or IDE
java -jar target/shippingAnalyticsBeamPipeline-bundled-1.0.jar --inputTopicSubscription="<<Input Topic Subscription>>" --bigQueryTable="<<BigQuery Table>>" --jobName="ShippingAnalyticsStream" --runner=DataflowRunner --gcpTempLocation="gs://<<GCP Location>>/ShippingAnalyticsTemp" --region=<<Region>> --zone=<<WorkerZone>>

# Build Flex template
gcloud dataflow flex-template build gs://<<GCP Bucket>>/dataflow/templates/shipping-analytics-stream-flex-template.json \
--image-gcr-path "<<Image Path>>/shipping-analytics:latest" \
--sdk-language "JAVA" \
--flex-template-base-image JAVA11 \
--jar "target/shippingAnalyticsBeamPipeline-bundled-1.0.jar" \
--env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.kloudbuddy.stream.pipeline.BeamPipeline"

# Run dataflow from flex template
gcloud dataflow flex-template run "shipping-analytics-`date +%Y%m%d-%H%M%S`" --template-file-gcs-location="gs://<<GCP Bucket>>/dataflow/templates/shipping-analytics-stream-flex-template.json" --parameters inputTopicSubscription="<<Input Topic Subscription>>" --parameters bigQueryTable="<<BigQuery Table>>" --enable-streaming-engine --service-account-email="<<ServiceAccount>>" --staging-location="gs://<<GCP Bucket>>/staging/" --temp-location="gs://<<Gcp Bucket>>/tempLocation/" --region="<<Region>>"

# ShippingDetails sample json
{
"shippingId": "shipping_123",
"customerId": "customer_xyz",
"customerAddressId": "customerAddress_abc"
}
