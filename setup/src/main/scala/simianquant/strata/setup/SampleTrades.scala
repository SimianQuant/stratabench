package simianquant.strata.setup

import com.opengamma.strata.basics.currency.Currency.{GBP, USD}
import com.opengamma.strata.basics.currency.{Currency, CurrencyAmount, FxRate}
import com.opengamma.strata.basics.date._
import com.opengamma.strata.basics.date.BusinessDayConventions.MODIFIED_FOLLOWING;
import com.opengamma.strata.basics.index.{IborIndices, OvernightIndices}
import com.opengamma.strata.basics.schedule.{Frequency, PeriodicSchedule, StubConvention}
import com.opengamma.strata.basics.value.ValueSchedule
import com.opengamma.strata.basics.StandardId
import com.opengamma.strata.product.common.{BuySell, PayReceive}
import com.opengamma.strata.product.payment.{BulletPayment, BulletPaymentTrade}
import com.opengamma.strata.product.{AttributeType, Trade, TradeInfo}
import com.opengamma.strata.product.fx.{FxSingle, FxSingleTrade, FxSwap, FxSwapTrade}
import com.opengamma.strata.product.fra.{Fra, FraTrade}
import com.opengamma.strata.product.swap.`type`.{FixedIborSwapConventions, FixedOvernightSwapConventions}
import com.opengamma.strata.product.swap._
import java.time.LocalDate

object SampleTrades {

  def fxForward: Trade = {
    val fx = FxSingle.of(CurrencyAmount.of(GBP, 10000), FxRate.of(GBP, USD, 1.62), LocalDate.of(2014, 9, 14))

    FxSingleTrade
      .builder()
      .product(fx)
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "1"))
          .addAttribute(AttributeType.DESCRIPTION, "GBP 10,000/USD @ 1.62 fwd")
          .counterparty(StandardId.of("example", "BigBankA"))
          .settlementDate(LocalDate.of(2014, 9, 15))
          .build())
      .build()
  }

  def fxSwap: Trade = {
    val swap = FxSwap.ofForwardPoints(CurrencyAmount.of(GBP, 10000),
                                      FxRate.of(GBP, USD, 1.62),
                                      0.03,
                                      LocalDate.of(2014, 6, 14),
                                      LocalDate.of(2014, 9, 14))

    FxSwapTrade
      .builder()
      .product(swap)
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "3"))
          .addAttribute(AttributeType.DESCRIPTION, "GBP 10,000/USD @ 1.62 swap")
          .counterparty(StandardId.of("example", "BigBankA"))
          .settlementDate(LocalDate.of(2014, 9, 15))
          .build())
      .build()
  }

  def fra: Trade = {

    val fra = Fra
      .builder()
      .buySell(BuySell.SELL)
      .index(IborIndices.USD_LIBOR_3M)
      .startDate(LocalDate.of(2014, 9, 12))
      .endDate(LocalDate.of(2014, 12, 12))
      .fixedRate(0.0125)
      .notional(10000000)
      .build();

    return FraTrade
      .builder()
      .product(fra)
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "1"))
          .addAttribute(AttributeType.DESCRIPTION, "0x3 FRA")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 14))
          .build())
      .build();
  }

  def bullet: Trade = {
    val bp = BulletPayment.builder
      .payReceive(PayReceive.PAY)
      .value(CurrencyAmount.of(GBP, 20000))
      .date(AdjustableDate.of(LocalDate.of(2014, 9, 16)))
      .build()

    BulletPaymentTrade
      .builder()
      .product(bp)
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "4"))
          .addAttribute(AttributeType.DESCRIPTION, "Bullet payment GBP 20,000")
          .counterparty(StandardId.of("example", "BigBankC"))
          .settlementDate(LocalDate.of(2014, 9, 16))
          .build())
      .build()
  }

  // swaps

  // create a vanilla fixed vs libor 3m swap
  def vanillaFixedVsLibor3mSwap: SwapTrade = {
    val tradeInfo = TradeInfo
      .builder()
      .id(StandardId.of("example", "1"))
      .addAttribute(AttributeType.DESCRIPTION, "Fixed vs Libor 3m")
      .counterparty(StandardId.of("example", "A"))
      .settlementDate(LocalDate.of(2014, 9, 12))
      .build()

    FixedIborSwapConventions.USD_FIXED_6M_LIBOR_3M.toTrade(
      tradeInfo,
      LocalDate.of(2014, 9, 12), // the start date
      LocalDate.of(2021, 9, 12), // the end date
      BuySell.BUY, // indicates wheter this trade is a buy or sell
      100000000, // the notional amount
      0.015
    ); // the fixed interest rate
  }

  // create a libor 3m vs libor 6m basis swap with spread
  def basisLibor3mVsLibor6mWithSpreadSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 8, 27))
          .endDate(LocalDate.of(2024, 8, 27))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_6M))
      .build()

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 8, 27))
          .endDate(LocalDate.of(2024, 8, 27))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(
        IborRateCalculation
          .builder()
          .index(IborIndices.USD_LIBOR_3M)
          .spread(ValueSchedule.of(0.001))
          .build())
      .build()

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "2"))
          .addAttribute(AttributeType.DESCRIPTION, "Libor 3m + spread vs Libor 6m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build()
  }

  // Create an overnight averaged vs libor 3m swap with spread
  def overnightAveragedWithSpreadVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000)

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2020, 9, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build()

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2020, 9, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(
        OvernightRateCalculation
          .builder()
          .dayCount(DayCounts.ACT_360)
          .index(OvernightIndices.USD_FED_FUND)
          .accrualMethod(OvernightAccrualMethod.AVERAGED)
          .spread(ValueSchedule.of(0.0025))
          .build())
      .build()

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "3"))
          .addAttribute(AttributeType.DESCRIPTION, "Fed Funds averaged + spread vs Libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build()
  }

  // Create a vanilla fixed vs libor 3m swap with fixing
  def fixedVsLibor3mWithFixingSwap: Trade = {
    val tradeInfo = TradeInfo
      .builder()
      .id(StandardId.of("example", "4"))
      .addAttribute(AttributeType.DESCRIPTION, "Fixed vs libor 3m (with fixing)")
      .counterparty(StandardId.of("example", "A"))
      .settlementDate(LocalDate.of(2013, 9, 12))
      .build()

    FixedIborSwapConventions.USD_FIXED_6M_LIBOR_3M.toTrade(
      tradeInfo,
      LocalDate.of(2013, 9, 12), // the start date
      LocalDate.of(2020, 9, 12), // the end date
      BuySell.BUY, // indicates wheter this trade is a buy or sell
      100000000, // the notional amount
      0.015
    ); // the fixed interest rate
  }

  // Create a fixed vs overnight swap with fixing
  def fixedVsOvernightWithFixingSwap: Trade = {
    val tradeInfo = TradeInfo
      .builder()
      .id(StandardId.of("example", "5"))
      .addAttribute(AttributeType.DESCRIPTION, "Fixed vs ON (with fixing)")
      .counterparty(StandardId.of("example", "A"))
      .settlementDate(LocalDate.of(2014, 1, 17))
      .build()

    FixedOvernightSwapConventions.USD_FIXED_TERM_FED_FUND_OIS.toTrade(
      tradeInfo,
      LocalDate.of(2014, 1, 17), // the start date
      LocalDate.of(2014, 3, 17), // the end date
      BuySell.BUY, // indicates wheter this trade is a buy or sell
      100000000, // the notional amount
      0.00123
    ); // the fixed interest rate
  }

  // Create a fixed vs libor 3m swap
  def stub3mFixedVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000)

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 6, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build()

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 6, 12))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.01, DayCounts.THIRTY_U_360))
      .build()

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "6"))
          .addAttribute(AttributeType.DESCRIPTION, "Fixed vs Libor 3m (3m short initial stub)")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  // Create a fixed vs libor 3m swap
  def stub1mFixedVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 7, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 7, 12))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.01, DayCounts.THIRTY_U_360))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "7"))
          .addAttribute(AttributeType.DESCRIPTION, "Fixed vs Libor 3m (1m short initial stub)")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  // Create a fixed vs libor 6m swap
  def interpolatedStub3mFixedVsLibor6mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 6, 12))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation
        .builder()
        .index(IborIndices.USD_LIBOR_6M)
        .initialStub(IborRateStubCalculation.ofIborInterpolatedRate(IborIndices.USD_LIBOR_3M, IborIndices.USD_LIBOR_6M))
        .build())
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 6, 12))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.01, DayCounts.THIRTY_U_360))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "8"))
          .addAttribute(AttributeType.DESCRIPTION, "Fixed vs Libor 6m (interpolated 3m short initial stub)")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  // Create a fixed vs libor 6m swap
  def interpolatedStub4mFixedVsLibor6mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 7, 12))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation
        .builder()
        .index(IborIndices.USD_LIBOR_6M)
        .initialStub(IborRateStubCalculation.ofIborInterpolatedRate(IborIndices.USD_LIBOR_3M, IborIndices.USD_LIBOR_6M))
        .build())
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2016, 7, 12))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.01, DayCounts.THIRTY_U_360))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "9"))
          .addAttribute(AttributeType.DESCRIPTION, "Fixed vs Libor 6m (interpolated 4m short initial stub)")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  def zeroCouponFixedVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2021, 9, 12))
          .frequency(Frequency.P12M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.TERM)
          .paymentDateOffset(DaysAdjustment.NONE)
          .compoundingMethod(CompoundingMethod.STRAIGHT)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.015, DayCounts.THIRTY_U_360))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2021, 9, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.TERM)
          .paymentDateOffset(DaysAdjustment.NONE)
          .compoundingMethod(CompoundingMethod.STRAIGHT)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "10"))
          .addAttribute(AttributeType.DESCRIPTION, "Zero-coupon fixed vs libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  // Create a compounding fixed vs fed funds swap
  def compoundingFixedVsFedFundsSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 2, 5))
          .endDate(LocalDate.of(2014, 4, 7))
          .frequency(Frequency.TERM)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.TERM)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(FixedRateCalculation.of(0.00123, DayCounts.ACT_360))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 2, 5))
          .endDate(LocalDate.of(2014, 4, 7))
          .frequency(Frequency.TERM)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .stubConvention(StubConvention.SHORT_INITIAL)
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.TERM)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(OvernightRateCalculation.of(OvernightIndices.USD_FED_FUND))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "11"))
          .addAttribute(AttributeType.DESCRIPTION, "Compounding fixed vs fed funds")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 2, 5))
          .build())
      .build();
  }

  // Create a compounding fed funds vs libor 3m swap
  def compoundingFedFundsVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2020, 9, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 9, 12))
          .endDate(LocalDate.of(2020, 9, 12))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(
        OvernightRateCalculation
          .builder()
          .index(OvernightIndices.USD_FED_FUND)
          .accrualMethod(OvernightAccrualMethod.AVERAGED)
          .build())
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "12"))
          .addAttribute(AttributeType.DESCRIPTION, "Compounding fed funds vs libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 9, 12))
          .build())
      .build();
  }

  // Create a compounding libor 6m vs libor 3m swap
  def compoundingLibor6mVsLibor3mSwap: Trade = {
    val notional = NotionalSchedule.of(Currency.USD, 100000000);

    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 8, 27))
          .endDate(LocalDate.of(2024, 8, 27))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_6M))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 8, 27))
          .endDate(LocalDate.of(2024, 8, 27))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .compoundingMethod(CompoundingMethod.STRAIGHT)
          .build())
      .notionalSchedule(notional)
      .calculation(IborRateCalculation.of(IborIndices.USD_LIBOR_3M))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "13"))
          .addAttribute(AttributeType.DESCRIPTION, "Compounding libor 6m vs libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 8, 27))
          .build())
      .build();
  }

  // create a cross-currency GBP libor 3m vs USD libor 3m swap with spread
  def xCcyGbpLibor3mVsUsdLibor3mSwap: Trade = {
    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.GBLO))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(NotionalSchedule.of(Currency.GBP, 61600000))
      .calculation(IborRateCalculation.of(IborIndices.GBP_LIBOR_3M))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.USNY))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(NotionalSchedule.of(Currency.USD, 100000000))
      .calculation(
        IborRateCalculation.builder().index(IborIndices.USD_LIBOR_3M).spread(ValueSchedule.of(0.0091)).build())
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(receiveLeg, payLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "14"))
          .addAttribute(AttributeType.DESCRIPTION, "GBP Libor 3m vs USD Libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 1, 24))
          .build())
      .build();
  }

  // create a cross-currency USD fixed vs GBP libor 3m swap
  def xCcyUsdFixedVsGbpLibor3mSwap: Trade = {
    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.GBLO))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(NotionalSchedule.of(Currency.USD, 100000000))
      .calculation(FixedRateCalculation.of(0.03, DayCounts.THIRTY_U_360))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.GBLO))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(NotionalSchedule.of(Currency.GBP, 61600000))
      .calculation(IborRateCalculation.of(IborIndices.GBP_LIBOR_3M))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "15"))
          .addAttribute(AttributeType.DESCRIPTION, "USD fixed vs GBP Libor 3m")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 1, 24))
          .build())
      .build();
  }

  // create a cross-currency USD fixed vs GBP libor 3m swap with initial and final notional exchange
  def notionalExchangeSwap: Trade = {
    val payLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.PAY)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P6M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.GBLO))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P6M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(
        NotionalSchedule
          .builder()
          .currency(Currency.USD)
          .amount(ValueSchedule.of(100000000))
          .initialExchange(true)
          .finalExchange(true)
          .build())
      .calculation(FixedRateCalculation.of(0.03, DayCounts.THIRTY_U_360))
      .build();

    val receiveLeg = RateCalculationSwapLeg
      .builder()
      .payReceive(PayReceive.RECEIVE)
      .accrualSchedule(
        PeriodicSchedule
          .builder()
          .startDate(LocalDate.of(2014, 1, 24))
          .endDate(LocalDate.of(2021, 1, 24))
          .frequency(Frequency.P3M)
          .businessDayAdjustment(BusinessDayAdjustment.of(MODIFIED_FOLLOWING, HolidayCalendarIds.GBLO))
          .build())
      .paymentSchedule(
        PaymentSchedule
          .builder()
          .paymentFrequency(Frequency.P3M)
          .paymentDateOffset(DaysAdjustment.NONE)
          .build())
      .notionalSchedule(
        NotionalSchedule
          .builder()
          .currency(Currency.GBP)
          .amount(ValueSchedule.of(61600000))
          .initialExchange(true)
          .finalExchange(true)
          .build())
      .calculation(IborRateCalculation.of(IborIndices.GBP_LIBOR_3M))
      .build();

    SwapTrade
      .builder()
      .product(Swap.of(payLeg, receiveLeg))
      .info(
        TradeInfo
          .builder()
          .id(StandardId.of("example", "16"))
          .addAttribute(AttributeType.DESCRIPTION, "USD fixed vs GBP Libor 3m (notional exchange)")
          .counterparty(StandardId.of("example", "A"))
          .settlementDate(LocalDate.of(2014, 1, 24))
          .build())
      .build();
  }

}
