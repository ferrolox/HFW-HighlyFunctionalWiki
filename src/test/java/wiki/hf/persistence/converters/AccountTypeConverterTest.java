package wiki.hf.persistence.converters;

import wiki.hf.domain.*;
import wiki.hf.persistence.exceptions.DataQualityException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class AccountTypeConverterTest
{
    private AccountTypeConverter converter;

    @BeforeEach
    void Setup()
    {
        converter = new AccountTypeConverter();
    }

    @Test
    void convertValidAttributeToColumn()
    {
        assertThat(converter.convertToDatabaseColumn(AccountType.READER)).isEqualTo("READER");
        assertThat(converter.convertToDatabaseColumn(AccountType.EDITOR)).isEqualTo("EDITOR");
        assertThat(converter.convertToDatabaseColumn(AccountType.ADMINISTRATOR)).isEqualTo("ADMINISTRATOR");
        assertThat(converter.convertToDatabaseColumn(AccountType.OWNER)).isEqualTo("OWNER");
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    void convertValidColumnToAttribute()
    {
        assertThat(converter.convertToEntityAttribute("READER")).isEqualTo(AccountType.READER);
        assertThat(converter.convertToEntityAttribute("EDITOR")).isEqualTo(AccountType.EDITOR);
        assertThat(converter.convertToEntityAttribute("ADMINISTRATOR")).isEqualTo(AccountType.ADMINISTRATOR);
        assertThat(converter.convertToEntityAttribute("OWNER")).isEqualTo(AccountType.OWNER);
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void throwExceptionForInvalidColumn()
    {
        var exception = assertThrows(DataQualityException.class, () -> converter.convertToEntityAttribute("X"));
        assertThat(exception).hasMessage("\"X\" is not a valid value for Enumerator AccountType.");
    }
}