package com.lfhardware.charges.service;

import co.omise.Client;
import co.omise.ClientException;
import co.omise.models.*;
import co.omise.requests.Request;
import co.omise.requests.RequestBuilder;
import com.lfhardware.charges.dto.BankCode;
import com.lfhardware.charges.dto.ChargeResponse;
import com.lfhardware.charges.dto.OmiseProperties;
import com.lfhardware.charges.dto.Order;
import com.lfhardware.shared.Currency;
import com.lfhardware.shared.PaymentMethod;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OmisePaymentService implements IPaymentService{

    private final OmiseProperties omiseProperties;

    public static final Logger LOGGER = LoggerFactory.getLogger(OmisePaymentService.class);

    public OmisePaymentService(OmiseProperties omiseProperties){
        this.omiseProperties = omiseProperties;
    }
    @Override
    public Mono<ChargeResponse> charge(PaymentMethod paymentMethod,String bankCode, Order order) throws ClientException, IOException, OmiseException {

        LOGGER.info("Using payment method :{} ,bank code :{}",paymentMethod,bankCode);

        Client client = new Client.Builder()
                .publicKey(omiseProperties.getPublicKey())
                .secretKey(omiseProperties.getSecretKey())
                .build();

        return switch(paymentMethod){

            case TNG->{

                Item item = new Item();
                item.itemUri = "asdas";

                //Create new source for payment
                Request<Source> sourceRequest = new Source.CreateRequestBuilder()
                        .type(SourceType.TouchNGo)
                        .platformType(PlatformType.Web)
                        .currency(Currency.MYR.name())
                        .amount(order.getTotalAmount().longValue())
                        .mobileNumber(order.getPayer().getPhoneNumber())
                        .email(order.getPayer().getEmailAddress())
                        .name(order.getPayer().getName())
                        .addItem(item)
                        .build();

                Source source = client.sendRequest(sourceRequest);
                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(source.getAmount())
                        .currency(source.getCurrency())
                        .source(source.getId())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = client.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());
            }


            case DuitNowOBW -> {
                Request<Source> sourceRequest = new Source.CreateRequestBuilder()
                        .type(SourceType.DuitNowOBW)
                        .platformType(PlatformType.Web)
                        .currency(Currency.MYR.name())
                        .amount(order.getTotalAmount().longValue())
                        .mobileNumber(order.getPayer().getPhoneNumber())
                        .email(order.getPayer().getEmailAddress())
                        .name(order.getPayer().getName())
                        .bank(bankCode)
                        .build();

                Source source = client.sendRequest(sourceRequest);
                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(source.getAmount())
                        .currency(source.getCurrency())
                        .source(source.getId())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = client.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());
            }
            case CREDIT_CARD -> {

                //Create new charge with the source given.
                Request<Charge> chargeRequest = new Charge.CreateRequestBuilder()
                        .amount(order.getTotalAmount().longValue())
                        .currency(Currency.MYR.name())
                        .card(order.getPayer().getCreditCard())
                        .returnUri("http://localhost:4200")
                        .expiresAt(DateTime.now().plusMinutes(1))
                        .build();

                Charge charge = client.sendRequest(chargeRequest);

                yield Mono.just(ChargeResponse.builder().authorizeUri(charge.getAuthorizeUri()).build());
            }
        };
    }

    public Mono<List<BankCode>> findAllAvailableBank(SourceType sourceType) {

        return switch(sourceType){

            case Unknown -> null;
            case Alipay -> null;
            case BarcodeAlipay -> null;
            case BarcodeWechat -> null;
            case BillPaymentTescoLotus -> null;
            case Econtext -> null;
            case Fpx -> null;
            case InstallmentBay -> null;
            case InstallmentBbl -> null;
            case InstallmentFirstChoice -> null;
            case InstallmentKbank -> null;
            case InstallmentKtc -> null;
            case InstallmentScb -> null;
            case InstallmentUob -> null;
            case InstallmentTtb -> null;
            case InternetBankingBay -> null;
            case InternetBankingBbl -> null;
            case InternetBankingKtb -> null;
            case InternetBankingScb -> null;
            case MobileBankingBay -> null;
            case MobileBankingBbl -> null;
            case MobileBankingKbank -> null;
            case MobileBankingOcbcPao -> null;
            case MobileBankingScb -> null;
            case MobileBankingKtb -> null;
            case Paynow -> null;
            case PointsCiti -> null;
            case PromptPay -> null;
            case RabbitLinepay -> null;
            case TrueMoney -> null;
            case AlipayCN -> null;
            case AlipayHK -> null;
            case DANA -> null;
            case GCash -> null;
            case KakaoPay -> null;
            case TouchNGo -> null;
            case Atome -> null;
            case DuitNowOBW ->
                    Mono.just(List.of(
                    BankCode.builder().code("affin").name("Affin Bank").build(),
                    BankCode.builder().code("alliance").name("Alliance Bank").build(),
                    BankCode.builder().code("agro").name("AGRONet").build(),
                    BankCode.builder().code("ambank").name("Am Bank").build(),
                    BankCode.builder().code("islam").name("Bank Islam").build(),
                    BankCode.builder().code("muamalat").name("Bank Muamalat").build(),
                    BankCode.builder().code("rakyat").name("Bank Rakyat").build(),
                    BankCode.builder().code("bsn").name("BSN").build(),
                    BankCode.builder().code("cimb").name("CIMB Clicks").build(),
                    BankCode.builder().code("hongleong").name("Hong Leong").build(),
                    BankCode.builder().code("kfh").name("KFH").build(),
                    BankCode.builder().code("maybank2u").name("Maybank2U").build(),
                    BankCode.builder().code("ocbc").name("OCBC Bank").build(),
                    BankCode.builder().code("public").name("Public Bank").build(),
                    BankCode.builder().code("rhb").name("RHB Bank").build(),
                    BankCode.builder().code("sc").name("Standard Chartered").build(),
                    BankCode.builder().code("uob").name("UOB Bank").build()
                    ));

        };
    }
}
