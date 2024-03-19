package org.jk.esked.app.backend.services;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Service
public class TranslationProvider implements I18NProvider {
    public static final String BUNDLE_PREFIX = "translate";
    public static final Locale LOCALE_PL = new Locale("pl", "PL");
    public static final Locale LOCALE_EN = new Locale("en", "US");
    private static final Logger log = LoggerFactory.getLogger(TranslationProvider.class);

    private final List<Locale> locales = List.of(LOCALE_PL, LOCALE_EN);

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            log.warn("key not found for translation");
            return "";
        }

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = resourceBundle.getString(key);
        } catch (final MissingResourceException ex) {
            log.error("missing resource: " + ex.getMessage());
            return "!" + locale.getLanguage() + ": " + key;
        }

        if (params.length > 0) value = MessageFormat.format(value, params);

        return value;
    }
}
