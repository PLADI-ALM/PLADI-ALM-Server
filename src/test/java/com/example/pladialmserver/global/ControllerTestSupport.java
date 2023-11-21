package com.example.pladialmserver.global;

import com.example.pladialmserver.booking.controller.car.CarBookingAdminController;
import com.example.pladialmserver.booking.controller.car.CarBookingController;
import com.example.pladialmserver.booking.controller.office.OfficeBookingAdminController;
import com.example.pladialmserver.booking.controller.office.OfficeBookingController;
import com.example.pladialmserver.booking.controller.resource.ResourceBookingAdminController;
import com.example.pladialmserver.booking.controller.resource.ResourceBookingController;
import com.example.pladialmserver.booking.service.CarBookingService;
import com.example.pladialmserver.booking.service.OfficeBookingService;
import com.example.pladialmserver.booking.service.ResourceBookingService;
import com.example.pladialmserver.equipment.controller.EquipmentController;
import com.example.pladialmserver.equipment.service.EquipmentService;
import com.example.pladialmserver.global.resolver.LoginResolver;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.office.controller.OfficeAdminController;
import com.example.pladialmserver.office.controller.OfficeController;
import com.example.pladialmserver.office.service.OfficeService;
import com.example.pladialmserver.product.resource.controller.ResourceAdminController;
import com.example.pladialmserver.product.resource.controller.ResourceController;
import com.example.pladialmserver.product.resource.service.ResourceService;
import com.example.pladialmserver.user.controller.AdminUserController;
import com.example.pladialmserver.user.controller.UserController;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                UserController.class,
                AdminUserController.class,
                ResourceController.class,
                ResourceAdminController.class,
                OfficeController.class,
                OfficeAdminController.class,
                EquipmentController.class,
                OfficeBookingAdminController.class,
                ResourceBookingAdminController.class,
                CarBookingAdminController.class,
                OfficeBookingController.class,
                ResourceBookingController.class,
                CarBookingController.class
        }
)
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ControllerTestSupport {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @MockBean protected LoginResolver loginResolver;
    @MockBean protected JwtUtil jwtUtil;
    @MockBean protected User user;
    @MockBean protected UserService userService;
    @MockBean protected OfficeService officeService;
    @MockBean protected ResourceService resourceService;
    @MockBean protected OfficeBookingService officeBookingService;
    @MockBean protected ResourceBookingService resourceBookingService;
    @MockBean protected CarBookingService carBookingService;
    @MockBean protected EquipmentService equipmentService;

}
