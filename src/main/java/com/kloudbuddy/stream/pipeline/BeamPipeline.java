package com.kloudbuddy.stream.pipeline;

import com.google.api.services.bigquery.model.TableRow;
import com.google.gson.Gson;
import com.kloudbuddy.stream.model.ShippingDetails;
import com.kloudbuddy.stream.options.RuntimeOptions;
import com.kloudbuddy.stream.utils.BigQuerySchemaUtils;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

public class BeamPipeline {
    public static void main(String[] args) {
        RuntimeOptions options =
                PipelineOptionsFactory.fromArgs(args).withValidation().as(RuntimeOptions.class);
        Pipeline pipeline = Pipeline.create(options);
        pipeline
                .apply("Read PubSub Messages", PubsubIO.readStrings()
                        .fromSubscription(options.getInputTopicSubscription()))
                .apply(ParDo.of(new DoFn<String, ShippingDetails>() {
                    @ProcessElement
                    public void processElement(ProcessContext processContext) {
                        processContext.output(new Gson().fromJson(processContext.element(), ShippingDetails.class));
                    }
                }))
                .apply(BigQueryIO.<ShippingDetails>write()
                        .to(options.getBigQueryTable())
                        .withSchema(BigQuerySchemaUtils.createSchema())
                        .withFormatFunction((ShippingDetails shippingDetail) ->
                                new TableRow().set("shippingId", shippingDetail.getShippingId())
                                        .set("customerId", shippingDetail.getCustomerId())
                                        .set("customerAddressId", shippingDetail.getCustomerAddressId()))
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));
        pipeline.run();
    }
}
