package rw.qt.userms.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.dtos.*;
import rw.qt.userms.services.IUserService;
import rw.qt.userms.utils.Constants;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.domains.ApiResponse;
import rw.qt.userms.models.enums.EUserStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final IUserService userService;

    @GetMapping(value = "")
    public ResponseEntity<ApiResponse<Page<UserAccount>>> getAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws ResourceNotFoundException {
        Pageable pageable = (Pageable) PageRequest.of(page-1, limit, Sort.Direction.DESC,"id");
        Page<UserAccount> users = this.userService.getAllPaginated(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(users, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }



    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<UserAccount>> getById(@Valid @PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        UserAccount userAccount = this.userService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.getEntitySuccess"), HttpStatus.OK));
    }




    @PostMapping
    public ResponseEntity<ApiResponse<UserAccount>> create(@Valid @RequestBody CreateUserDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            UserAccount userAccount = this.userService.create(dto);
            return new ResponseEntity<>(new ApiResponse<>(userAccount, localize("responses.saveEntitySuccess"), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<UserAccount>> updateById(@PathVariable(value = "id") UUID id, @Valid @RequestBody UpdateUserDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            UserAccount userAccount = this.userService.updateById(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }




    @PutMapping(path="/{id}/updatePassword")
    public ResponseEntity<ApiResponse<UserAccount>> changePassword(@PathVariable(value = "id") UUID id, @Valid @RequestBody UpdatePasswordDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
        UserAccount userAccount = this.userService.updatePassword(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PutMapping(path="/{id}/unlock")
    public ResponseEntity<ApiResponse<UserAccount>> unlockUser(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        UserAccount userAccount = this.userService.unlockAccountById(id);
        return ResponseEntity.ok(new ApiResponse<>(userAccount, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }


    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        this.userService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(new Object(), localize("responses.deleteEntitySuccess"), null, HttpStatus.OK));
    }



    @Override
    protected String getEntityName() {
        return "User";
    }

}

