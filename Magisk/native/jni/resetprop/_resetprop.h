/* resetprop.h - Internal struct definitions
 */


#ifndef MAGISK_PROPS_H
#define MAGISK_PROPS_H

#include <CharArray.h>
#include "resetprop/private/system_properties.h"
#include "logging.h"

struct prop_t {
	char *name;
	char value[PROP_VALUE_MAX];
	prop_t() = default;
	prop_t(const char *name) {
		this->name = strdup(name);
		value[0] = '\0';
	}
	prop_t(const char *name, const char *value) {
		this->name = strdup(name);
		strcpy(this->value, value);
	}
	prop_t& operator= (prop_t&& prop) {
		if (this != &prop) {
			free(name);
			name = prop.name;
			strcpy(value, prop.value);
			prop.name = nullptr;
		}
		return *this;
	};
	~prop_t() {
		free(name);
	}
};

struct read_cb_t {
	void (*cb)(const char *, const char *, void *);
	void *arg;
	read_cb_t(void (*cb)(const char *, const char *, void *) = nullptr, void *arg = nullptr)
			: cb(cb), arg(arg) {}
	void exec(const char *name, const char *value) {
		cb(name, value, arg);
	}
};

#define PERSISTENT_PROPERTY_DIR  "/data/property"

extern bool use_pb;

CharArray persist_getprop(const char *name);
void persist_getprop(read_cb_t *read_cb);
bool persist_deleteprop(const char *name);
void collect_props(const char *name, const char *value, void *v_plist);

#endif //MAGISK_PROPS_H
