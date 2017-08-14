package com.mis.ncyu.quickchoice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

/**
 * Created by UserMe on 2017/7/25.
 */

public class tab1_fragment extends Fragment {

    private Button btntest;
    int MY_SCAN_REQUEST_CODE = 1;
    TextView tab;
    private ImageView mResultImage;
    private ImageView mResultCardTypeImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        mResultImage = (ImageView) view.findViewById(R.id.imageView);
        mResultCardTypeImage = (ImageView)view.findViewById(R.id.imageView2);
        btntest = (Button)view.findViewById(R.id.btntst);
        tab = (TextView)view.findViewById(R.id.texttab1);
        btntest.setOnClickListener(btntestonclick);
        return view;
    }
    View.OnClickListener btntestonclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onScanPress(view);
        }
    };
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE,"zh-Hant_TW");
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            Bitmap cardTypeImage = null;
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                CardType cardType = scanResult.getCardType();
                cardTypeImage = cardType.imageBitmap(getActivity());

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            Bitmap card = CardIOActivity.getCapturedCardImage(data);
            mResultImage.setImageBitmap(card);
            mResultCardTypeImage.setImageBitmap(cardTypeImage);
            tab.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
}

