package com.db.desafiovotacao.api.service;

import com.db.desafiovotacao.api.domain.Status;
import com.db.desafiovotacao.api.entity.Agenda;
import com.db.desafiovotacao.api.entity.Session;
import com.db.desafiovotacao.api.exception.AgendaNotFoundException;
import com.db.desafiovotacao.api.repository.AgendaRepository;
import com.db.desafiovotacao.api.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OpenSessionService implements OpenSessionServiceInterface{
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public Session openSession(UUID agendaId, Duration duration) throws AgendaNotFoundException {

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException("Agenda not found:" + agendaId));

        Session session = new Session();
        session.setAgenda(agenda);
        session.setDataBegin(LocalDateTime.now());
        session.setDataEnd(LocalDateTime.now().plus(duration != null ? duration : Duration.ofMinutes(1)));
        session.setStatus(Status.OPENED);

        return sessionRepository.save(session);
    }
}
