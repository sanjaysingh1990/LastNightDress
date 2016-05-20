package com.eowise.recyclerview.stickyheaders.samples.PurchaseAndSwap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.LndMore.LndLuxuryandDesignerAuthentication;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.R;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SwapCheckOutActivity extends AppCompatActivity {
    @Bind(R.id.sameadd)
    TextView sameadd;
    @Bind(R.id.newadd)
    TextView newadd;
    @Bind(R.id.heading)
    TextView heading;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.newaddressblock)
    LinearLayout newaddress;
    @Bind(R.id.sameaddressblock)
    TextView sameaddress;
    @Bind(R.id.samepayment)
    TextView samepayment;
    @Bind(R.id.newpayment)
    TextView newpayment;
    @Bind(R.id.cardno)
    EditText cardno;

    @Bind(R.id.sameaddrellayout)
    RelativeLayout sameaddrellayout;
    @Bind(R.id.newaddrellayout)
    RelativeLayout newaddrellayout;

    @Bind(R.id.samepayrellayout)
    RelativeLayout samepayrellayout;
    @Bind(R.id.newpayrellayout)
    RelativeLayout newpayrellayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_checkout);
        ButterKnife.bind(this);
        //setup font
        heading.setTypeface(SingleTon.robotobold);


        Spannable word = new SpannableString(getResources().getString(R.string.swap_protection));

        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(word);
        Spannable wordTwo = new SpannableString(" Click to Learn more.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#30beff")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //text.append(wordTwo);

        wordTwo.setSpan(new MyClickableSpan(wordTwo.toString()), 0,
                wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.append(wordTwo);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setHighlightColor(Color.TRANSPARENT);


    }

    /*
public void pickup(View v)
{
pickup.setBackgroundColor(Color.parseColor("#be4d66"));
pickup.setTextColor(Color.parseColor("#ffffff"));
delivery.setBackgroundResource(R.drawable.purchse_rounded_corners);
delivery.setTextColor(Color.parseColor("#dbdbdb"));
}
  public void delivery(View v)
  {
      delivery.setBackgroundColor(Color.parseColor("#be4d66"));
      delivery.setTextColor(Color.parseColor("#ffffff"));
      pickup.setBackgroundResource(R.drawable.purchse_rounded_corners);
      pickup.setTextColor(Color.parseColor("#dbdbdb"));
  }*/
    public void sameadd(View v) {
        this.sameaddrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newaddrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));

        this.sameadd.setBackgroundColor(Color.parseColor("#be4d66"));
        this.sameadd.setTextColor(Color.parseColor("#ffffff"));
        this.sameadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.newadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newadd.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.newadd.setTextColor(Color.parseColor("#be4d66"));
        this.newadd.setBackgroundColor(Color.parseColor("#dbdbdb"));
        newaddress.setVisibility(View.GONE);
        sameaddress.setVisibility(View.VISIBLE);
        if (sameadd.getText().toString().compareToIgnoreCase("cancel") == 0) {

            this.newadd.setText("New Address");
            this.sameadd.setText("Same Address");
        }

    }

    public void newadd(View v) {
        this.newaddrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.sameaddrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.newadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.sameadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newadd.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newadd.setTextColor(Color.parseColor("#ffffff"));
        this.sameadd.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.sameadd.setTextColor(Color.parseColor("#be4d66"));
        this.sameadd.setBackgroundColor(Color.parseColor("#dbdbdb"));
        newaddress.setVisibility(View.VISIBLE);
        sameaddress.setVisibility(View.GONE);
        if (newadd.getText().toString().compareToIgnoreCase("new address") == 0) {
            this.newadd.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.newadd.setText("Save");
            this.sameadd.setText("Cancel");
        } else {
            this.newadd.setText("New Address");
            this.sameadd.setText("Same Address");
            newaddress.setVisibility(View.GONE);
            sameaddress.setVisibility(View.VISIBLE);
            this.newadd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);

        }
    }

    public void newpayment(View v) {
        this.newpayrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.samepayrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.samepayment
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newpayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newpayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.samepayment.setTextColor(Color.parseColor("#be4d66"));
        this.samepayment.setBackgroundColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(true);
        if (newpayment.getText().toString().compareToIgnoreCase("new payment") == 0) {
            this.newpayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            this.newpayment.setText("Save");
            this.samepayment.setText("Cancel");
        } else {
            this.newpayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
            this.newpayment.setText("New Payment");
            this.samepayment.setText("Same Payment");

        }
    }

    public void samepayment(View v) {
        this.samepayrellayout.setBackgroundColor(Color.parseColor("#be4d66"));
        this.newpayrellayout.setBackgroundColor(Color.parseColor("#dbdbdb"));


        this.samepayment.setBackgroundColor(Color.parseColor("#be4d66"));
        this.samepayment.setTextColor(Color.parseColor("#ffffff"));
        this.samepayment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.payment_selection, 0, 0, 0);
        this.newpayment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        this.newpayment.setBackgroundResource(R.drawable.purchse_rounded_corners);
        this.newpayment.setTextColor(Color.parseColor("#be4d66"));
        this.newpayment.setBackgroundColor(Color.parseColor("#dbdbdb"));
        this.cardno.setEnabled(false);
        if (samepayment.getText().toString().compareToIgnoreCase("cancel") == 0) {
            this.newpayment.setText("New Payment");
            this.samepayment.setText("Same Payment");
        }

    }

    public void close(View v) {
        finish();
    }

    public void newPayment(View v) {

        //onBuyPressed();
        Intent swapstepone = new Intent(this, Swap_Checkout_Cancel_Activity.class);
        startActivity(swapstepone);
    }





    public void showAlert(String paymentdetatils) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SwapCheckOutActivity.this);
        View view = LayoutInflater.from(SwapCheckOutActivity.this).inflate(R.layout.paypal_payment_dialog_layout, null);
        TextView alertmsg = (TextView) view.findViewById(R.id.alertmessage);
        alertmsg.setText(paymentdetatils);
        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();

        view.findViewById(R.id.alertcontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                finish();

            }
        });
    }

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan(String string) {
            super();
        }

        public void onClick(View tv) {
            showDialog();
        }

        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.parseColor("#30beff"));

            ds.setUnderlineText(false); // set to false to remove underline
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.swap_protection_more_dialog, null);
        dialog.setView(view);
        final AlertDialog alert = dialog.create();
        alert.show();
        //cancel dialog
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        //taking reference and apply font
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView heading1 = (TextView) view.findViewById(R.id.heading1);
        TextView heading2 = (TextView) view.findViewById(R.id.heading2);
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        heading.setTypeface(SingleTon.robotobold);
        heading1.setTypeface(SingleTon.robotoregular);
        heading2.setTypeface(SingleTon.robotoregular);
        text1.setTypeface(SingleTon.robotoregular);
        text2.setTypeface(SingleTon.robotoregular);


    }

    public void learnmore(View v) {
        Intent luxurydesign = new Intent(this, LndLuxuryandDesignerAuthentication.class);
        startActivity(luxurydesign);
    }
    public void doPayment(View v)
    {
        setContentView(R.layout.lnd_purchase_newshipping_level);
    }
    public void back(View v)
    {
        finish();
    }
}
