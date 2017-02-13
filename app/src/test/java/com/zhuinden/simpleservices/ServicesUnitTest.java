package com.zhuinden.simpleservices;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ServicesUnitTest {
    @Test
    public void serviceFactoryBindsServices() {
        Object key = new Object();
        final Object service = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                builder.withService("HELLO", service);
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(key);

        assertThat(servicesManager.findServices(key).getService("HELLO")).isSameAs(service);
    }

    @Test
    public void unbindingServiceMakesItInaccessible() {
        Object key = new Object();
        final Object service = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                builder.withService("HELLO", service);
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(key);
        servicesManager.tearDown(key, false);

        try {
            servicesManager.findServices(key).getService("HELLO");
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }
    }

    @Test
    public void bindingChildCreatesServiceOfParent() {
        final Object parentKey = new Object();
        Services.Child childKey = new Services.Child() {
            @Override
            public Object parent() {
                return parentKey;
            }
        };
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == parentKey) {
                    builder.withService("HELLO", parentService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(childKey);

        assertThat(servicesManager.findServices(parentKey).getService("HELLO")).isSameAs(parentService);
    }

    @Test
    public void unbindingChildMakesParentServiceInaccessible() {
        final Object parentKey = new Object();
        Services.Child childKey = new Services.Child() {
            @Override
            public Object parent() {
                return parentKey;
            }
        };
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == parentKey) {
                    builder.withService("HELLO", parentService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(childKey);
        servicesManager.tearDown(childKey, false);

        try {
            servicesManager.findServices(parentKey).getService("HELLO");
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }
    }

    @Test
    public void childCanInheritParentServices() {
        final Object parentKey = new Object();
        Services.Child childKey = new Services.Child() {
            @Override
            public Object parent() {
                return parentKey;
            }
        };
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == parentKey) {
                    builder.withService("HELLO", parentService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(childKey);

        assertThat(servicesManager.findServices(parentKey).getService("HELLO")).isEqualTo(parentService);
        assertThat(servicesManager.findServices(childKey).getService("HELLO")).isEqualTo(parentService);
        assertThat(servicesManager.findServices(childKey).getService("HELLO")).isEqualTo(servicesManager.findServices(parentKey).getService("HELLO"));
    }

    @Test
    public void serviceNotFoundReturnsNull() {
        final Object parentKey = new Object();
        Services.Child childKey = new Services.Child() {
            @Override
            public Object parent() {
                return parentKey;
            }
        };
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == parentKey) {
                    builder.withService("HELLO", parentService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(childKey);

        assertThat(servicesManager.findServices(childKey).getService("WORLD")).isNull();
    }


    @Test
    public void tearingDownKeyMultipleTimesThrowsException() {
        final Object key = new Object();
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == key) {
                    builder.withService("HELLO", parentService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(key);
        assertThat(servicesManager.findServices(key).getService("HELLO")).isEqualTo(parentService);
        servicesManager.setUp(key);
        assertThat(servicesManager.findServices(key).getService("HELLO")).isEqualTo(parentService);
        servicesManager.tearDown(key, false);
        assertThat(servicesManager.findServices(key).getService("HELLO")).isEqualTo(parentService);
        servicesManager.tearDown(key, false);
        try {
            servicesManager.tearDown(key, false);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }
    }

    @Test
    public void compositeChildrenHaveServicesCreated() {
        class ChildWithParentField
                implements Services.Child {
            Services.Composite parent;

            @Override
            public Object parent() {
                return parent;
            }
        }
        final ChildWithParentField childA = new ChildWithParentField();
        final ChildWithParentField childB = new ChildWithParentField();
        final Services.Composite composite = new Services.Composite() {
            @Override
            public List<ChildWithParentField> keys() {
                return Arrays.asList(childA, childB);
            }
        };
        childA.parent = composite;
        childB.parent = composite;

        final Object childAService = new Object();
        final Object childBService = new Object();
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == composite) {
                    builder.withService("HELLO", parentService);
                } else if(builder.getKey() == childA) {
                    builder.withService("WORLD", childAService);
                } else if(builder.getKey() == childB) {
                    builder.withService("CROCODILES", childBService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(composite);

        assertThat(servicesManager.findServices(composite).getService("HELLO")).isSameAs(parentService);
        assertThat(servicesManager.findServices(childA).getService("WORLD")).isSameAs(childAService);
        assertThat(servicesManager.findServices(childB).getService("CROCODILES")).isSameAs(childBService);
        assertThat(servicesManager.findServices(childA).getService("HELLO")).isSameAs(parentService);
        assertThat(servicesManager.findServices(childB).getService("HELLO")).isSameAs(parentService);
    }

    @Test
    public void compositeChildrenHaveServicesTornDownWithParent() {
        class ChildWithParentField
                implements Services.Child {
            Services.Composite parent;

            @Override
            public Object parent() {
                return parent;
            }
        }
        final ChildWithParentField childA = new ChildWithParentField();
        final ChildWithParentField childB = new ChildWithParentField();
        final Services.Composite composite = new Services.Composite() {
            @Override
            public List<ChildWithParentField> keys() {
                return Arrays.asList(childA, childB);
            }
        };
        childA.parent = composite;
        childB.parent = composite;

        final Object childAService = new Object();
        final Object childBService = new Object();
        final Object parentService = new Object();
        List<ServicesFactory> servicesFactories = new ArrayList<>();
        servicesFactories.add(new ServicesFactory() {
            @Override
            public void bindServices(@NonNull Services.Builder builder) {
                if(builder.getKey() == composite) {
                    builder.withService("HELLO", parentService);
                } else if(builder.getKey() == childA) {
                    builder.withService("WORLD", childAService);
                } else if(builder.getKey() == childB) {
                    builder.withService("CROCODILES", childBService);
                }
            }
        });
        ServicesManager servicesManager = new ServicesManager(servicesFactories);
        servicesManager.setUp(composite);
        assertThat(servicesManager.findServices(composite).getService("HELLO")).isSameAs(parentService);
        assertThat(servicesManager.findServices(childA).getService("WORLD")).isSameAs(childAService);
        assertThat(servicesManager.findServices(childB).getService("CROCODILES")).isSameAs(childBService);
        assertThat(servicesManager.findServices(childA).getService("HELLO")).isSameAs(parentService);
        assertThat(servicesManager.findServices(childB).getService("HELLO")).isSameAs(parentService);

        servicesManager.tearDown(composite, false);
        try {
            assertThat(servicesManager.findServices(composite).getService("HELLO")).isSameAs(parentService);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }

        try {
            assertThat(servicesManager.findServices(childA).getService("WORLD")).isSameAs(childAService);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }

        try {
            assertThat(servicesManager.findServices(childB).getService("CROCODILES")).isSameAs(childBService);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }

        try {
            assertThat(servicesManager.findServices(childA).getService("HELLO")).isSameAs(parentService);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }

        try {
            assertThat(servicesManager.findServices(childB).getService("HELLO")).isSameAs(parentService);
            fail();
        } catch(IllegalStateException e) {
            // OK!
        }
    }
}