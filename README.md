# shippingAnalyticsBeamPipeline
Beam pipeline to stream shipping details from pubsub to BigQuery


java -jar target/shippingAnalyticsBeamPipeline-bundled-1.0.jar --inputTopicSubscription="projects/mkloud/subscriptions/shippingAnalyticsSubscription" --bigQueryTable="pubSubBigQueryDataSet.ShippingDetailsTable" --jobName="ShippingAnalyticsStream" --runner=DataflowRunner --gcpTempLocation="gs://mhk-dataflow-bucket/ShippingAnalyticsTemp" --region=northamerica-northeast1 --zone=northamerica-northeast1-b

gcloud dataflow flex-template build gs://mhk-dataflow-bucket-1/dataflow/templates/shipping-analytics-stream-flex-template.json \
--image-gcr-path "northamerica-northeast1-docker.pkg.dev/mkloud/mkloud-artifacts/dataflow/shipping-analytics:latest" \
--sdk-language "JAVA" \
--flex-template-base-image JAVA11 \
--jar "target/shippingAnalyticsBeamPipeline-bundled-1.0.jar" \
--env FLEX_TEMPLATE_JAVA_MAIN_CLASS="com.kloudbuddy.stream.pipeline.BeamPipeline"


gcloud dataflow flex-template run "shipping-analytics-`date +%Y%m%d-%H%M%S`" --template-file-gcs-location="gs://mhk-dataflow-bucket-1/dataflow/templates/shipping-analytics-stream-flex-template.json" --parameters inputTopicSubscription="projects/mkloud/subscriptions/shippingAnalyticsSubscription" --parameters bigQueryTable="pubSubBigQueryDataSetUsCentral.ShippingDetailsTable" --enable-streaming-engine --service-account-email="dataflow-sa@mkloud.iam.gserviceaccount.com" --staging-location="gs://mhk-dataflow-bucket-1/staging/" --temp-location="gs://mhk-dataflow-bucket-1/tempLocation/" --region="us-central1"

ShippingDetails sample json
{
"shippingId": "shipping_123",
"customerId": "customer_xyz",
"customerAddressId": "customerAddress_abc"
}