package com.minsait.junit.models;

import com.minsait.junit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CuentaTest {

    Cuenta cuenta;
    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeEach
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Cuenta("Elias", new BigDecimal("1000"));
        testReporter.publishEntry("Iniciando el metodo");
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        testReporter.publishEntry("Ejecutando: " + testInfo.getTestMethod().orElse(null).getName());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Iniciando todos los test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando todos los test");
    }

    @Nested
    @DisplayName("Test del nombre y el saldo")
    class CuentaNombreSaldoTest{
        @Test
        void testNombreCuenta() {
            String esperado = "Elias";
            String real = cuenta.getPersona();
            assertNotNull(real);
            assertEquals(esperado,real);
            assertTrue(real.equalsIgnoreCase(esperado));
        }

        @Test
        void testSaldoCuenta(){
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertEquals(1000, cuenta.getSaldo().intValue());
        }

        @Test
        void testReferencia(){
            Cuenta cuenta2 = new Cuenta("Elias",new BigDecimal("1000"));
            assertEquals(cuenta2, cuenta);
        }
    }

    @Nested
    class CuentaOperacionesTest{
        @Test
        void testRetirarCuenta(){
            cuenta.retirar(new BigDecimal(500));
            assertNotNull(cuenta.getSaldo());
            assertEquals(500,cuenta.getSaldo().doubleValue());
        }
        @Test
        void testDepositarCuenta(){
            cuenta.depositar(new BigDecimal(500));
            assertEquals(1500,cuenta.getSaldo().doubleValue());
        }
        @Test
        void testTransferirEntreCuentas(){
            Cuenta origen = new Cuenta("Elias", new BigDecimal(500));
            Cuenta destino = new Cuenta("Elias", new BigDecimal(200));
            Banco banco = new Banco();
            banco.setNombre("BBVA");
            banco.transferir(origen,destino,new BigDecimal(250));
            assertEquals(450, destino.getSaldo().intValue());
        }
    }

    @Test
    void testException(){
        Exception exception = assertThrows(DineroInsuficienteException.class, ()->{
            cuenta.retirar(new BigDecimal(1500));
        });
        assertEquals("Dinero Insuficiente",exception.getMessage());
        assertTrue(exception instanceof DineroInsuficienteException);

    }


    @Test
    @DisplayName("Test relacion entre banco y cuentas")
    void restRelacionBancoCuenta(){
        Cuenta cuenta2 = new Cuenta("Test", new BigDecimal(10000));
        Banco banco = new Banco();
        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);
        banco.setNombre("BBVA");
        banco.transferir(cuenta2, cuenta, new BigDecimal(9000));
        assertAll(
                () -> assertEquals(10000,cuenta.getSaldo().intValue()),
                () -> assertEquals(1000,cuenta2.getSaldo().intValue()),
                () -> assertEquals(2,banco.getCuentas().size()),
                () -> assertEquals(banco.getNombre(),cuenta.getBanco().getNombre()),
                () -> assertEquals(banco.getNombre(),cuenta2.getBanco().getNombre()),
                () -> assertEquals(cuenta.getPersona(),
                        banco.getCuentas().stream().filter(e -> e.equals(cuenta))
                                .findFirst().get().getPersona()
                )
        );
    }

}